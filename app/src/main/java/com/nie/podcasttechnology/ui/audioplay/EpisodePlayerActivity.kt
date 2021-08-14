package com.nie.podcasttechnology.ui.audioplay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.bean.AudioPlayer
import com.nie.podcasttechnology.bean.AudioPlayerState
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.databinding.ActivityAudioPlayBinding
import com.nie.podcasttechnology.extension.throttleClick
import com.nie.podcasttechnology.extension.toFormatTimeStr
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodePlayerActivity : BaseActivity() {

    companion object {
        private const val KEY_PODCAST_ITEM = "key_podcast_item"

        fun start(activity: Activity, item: EntityEpisode) {
            Intent(activity, EpisodePlayerActivity::class.java).apply {
                putExtra(KEY_PODCAST_ITEM, item)
            }.let { activity.startActivity(it) }
        }
    }

    private val binding by lazy { ActivityAudioPlayBinding.inflate(layoutInflater) }

    override val viewModel by viewModel<EpisodePlayerViewModel>()

    private val podcastItem by lazy { intent.getSerializableExtra(KEY_PODCAST_ITEM) as EntityEpisode }

    private val audioPlayer by inject<AudioPlayer>()

    private lateinit var audioState: AudioPlayerState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycle.addObserver(audioPlayer)

        initView()
        initAudioPlayer()
        initSeekBarListener()
        setOnClickListener()
        observableLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(audioPlayer)
    }

    private fun initView() {
        Glide.with(this)
            .load(podcastItem.imageUrl)
            .placeholder(R.drawable.place_holder_grey)
            .into(binding.imagePodcastCover)

        binding.textTitle.text = podcastItem.title
    }

    private fun initAudioPlayer() {
        audioPlayer.resetPlayer(this, podcastItem.pubDate, podcastItem.audioUrl)

        audioPlayer.getPlayerStateListener()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                audioState = state

                when (state) {
                    is AudioPlayerState.Prepare -> {
                        initSeekBarMax(state.maxDuration)
                        setMaxDurationText(state.maxDuration / 1000)
                    }

                    is AudioPlayerState.Playing -> {
                        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_circle_filled_24)
                        binding.imageAudioController.setImageDrawable(drawable)

                        updateSeekBarProgress(state.position)
                        setCurrentDurationText(state.position / 1000)
                    }

                    is AudioPlayerState.Finished -> {
                        viewModel.getNextPodcast(state.currentPubDate)
                    }

                    is AudioPlayerState.Paused -> {
                        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_circle_filled_24)
                        binding.imageAudioController.setImageDrawable(drawable)
                    }
                }
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)

        audioPlayer.getPlayerBufferProgressListener()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateSecondaryProgress(it)
            }, {
                it.printStackTrace()
            }).addTo(compositeDisposable)
    }

    private fun setMaxDurationText(maxDuration: Int) {
        binding.textMaxDuration.text = maxDuration.toFormatTimeStr()
    }

    private fun setCurrentDurationText(currentPosition: Int) {
        val str = "${currentPosition.toFormatTimeStr()} / "
        binding.textCurrentDuration.text = str
    }

    private fun initSeekBarMax(duration: Int) {
        binding.seekBar.max = duration
    }

    private fun updateSeekBarProgress(currentPosition: Int) {
        binding.seekBar.progress = currentPosition
    }

    private fun updateSecondaryProgress(progress: Int) {
        binding.seekBar.secondaryProgress = progress
    }

    private fun initSeekBarListener() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioPlayer.playerSeekTo(progress)
                    binding.seekBar.progress = progress
                    setCurrentDurationText(audioPlayer.getCurrentPosition()!! / 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun setOnClickListener() {
        RxView.clicks(binding.imageAudioController)
            .throttleClick()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (audioState is AudioPlayerState.Playing) {
                    audioPlayer.pauseAudio()
                } else {
                    audioPlayer.startAudio()
                }
            }.addTo(compositeDisposable)

        RxView.clicks(binding.imageForward)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                audioPlayer.forward()
            }.addTo(compositeDisposable)

        RxView.clicks(binding.imageRewind)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                audioPlayer.rewind()
            }.addTo(compositeDisposable)
    }

    private fun observableLiveData() {
        viewModel.episode.observe(this, {
            if (it.isEmpty()) {
                audioPlayer.destroyAudio()
                return@observe
            }

            val entityPodcast = it.first()
            binding.textTitle.text = entityPodcast.title
            audioPlayer.resetPlayer(this, entityPodcast.pubDate, entityPodcast.audioUrl)
        })
    }
}