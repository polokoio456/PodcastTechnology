package com.nie.podcasttechnology.bean

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.nie.podcasttechnology.util.Constant
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import java.util.concurrent.TimeUnit

class AudioPlayer : LifecycleObserver {
    private val playerSubject by lazy { PublishSubject.create<AudioPlayerState>() }
    private val playerBufferingLevelSubject by lazy { PublishSubject.create<Int>() }

    private val compositeDisposable = CompositeDisposable()

    private var player: MediaPlayer? = null

    private var isPrepared = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startAudio() {
        if (player != null && player!!.isPlaying) {
            return
        }

        if (player == null) {
            return
        }

        if (isPrepared.not()) {
            return
        }

        player!!.start()

        if (player!!.duration > 0) {
            Flowable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe({
                    player?.currentPosition?.let {
                        playerSubject.onNext(AudioPlayerState.Playing(it))

                        if (it + 10 >= player!!.duration) {
                            playerSubject.onNext(AudioPlayerState.Finished)
                            releasePlayer()
                        }
                    }
                }, {
                    it.printStackTrace()
                }).addTo(compositeDisposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun pauseAudio() {
        compositeDisposable.clear()
        player?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyAudio() {
        isPrepared = false
        releasePlayer()
    }

    fun resetPlayer(context: Context, url: String) {
        player = MediaPlayer()

        player!!.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        player!!.reset()

        try {
            val uri = Uri.parse(url)
            player!!.setDataSource(context, uri)
        } catch (e: Exception) {
            Log.e(Constant.TAG, e.message.toString())
            e.printStackTrace()
        }

        player!!.setOnPreparedListener {
            isPrepared = true
            playerSubject.onNext(AudioPlayerState.Prepare(it.duration))
            startAudio()

            player?.currentPosition?.let { currentPosition ->
                playerSubject.onNext(AudioPlayerState.Playing(currentPosition))
            }
        }

        player!!.setOnBufferingUpdateListener { mp, percent ->
            val ratio = percent / 100.0
            val bufferProgress = (mp.duration * ratio).toInt()
            playerBufferingLevelSubject.onNext(bufferProgress)
        }

        player!!.prepareAsync()
    }

    fun playerSeekTo(progress: Int) {
        player?.seekTo(progress)
    }

    fun getPlayerStateListener(): Flowable<AudioPlayerState> = playerSubject.toFlowable(BackpressureStrategy.BUFFER)
    fun getPlayerBufferProgressListener(): Flowable<Int> = playerBufferingLevelSubject.toFlowable(BackpressureStrategy.BUFFER)

    private fun releasePlayer() {
        Log.d(Constant.TAG, "releasePlayer...")
        player?.stop()
        player?.reset()
        player?.release()
        player = null
    }
}

sealed class AudioPlayerState {
    data class Prepare(val maxDuration: Int) : AudioPlayerState()
    data class Playing(val position: Int) : AudioPlayerState()
    object Finished : AudioPlayerState()
}