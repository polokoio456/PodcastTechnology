package com.nie.podcasttechnology.ui.audioplay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.bean.AudioPlayer
import com.nie.podcasttechnology.bean.AudioPlayerState
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import com.nie.podcasttechnology.databinding.ActivityAudioPlayBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayActivity : BaseActivity() {

    companion object {
        private const val KEY_PODCAST_ITEM = "key_podcast_item"

        fun start(activity: Activity, item: PodcastItem) {
            Intent(activity, AudioPlayActivity::class.java).apply {
                putExtra(KEY_PODCAST_ITEM, item)
            }.let { activity.startActivity(it) }
        }
    }

    private val binding by lazy { ActivityAudioPlayBinding.inflate(layoutInflater) }

    override val viewModel by viewModel<AudioPlayViewModel>()

    private val podcastItem by lazy { intent.getSerializableExtra(KEY_PODCAST_ITEM) as PodcastItem }

    private val audioPlayer by inject<AudioPlayer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycle.addObserver(audioPlayer)

        initView()
        initAudioPlayer()
        initSeekBarListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(audioPlayer)
    }

    private fun initView() {
        Glide.with(this)
            .load(podcastItem.image.imageUrl)
            .placeholder(R.drawable.place_holder_grey)
            .into(binding.imagePodcastCover)

        binding.textTitle.text = podcastItem.title
    }

    private fun initAudioPlayer() {
        audioPlayer.resetPlayer(this, podcastItem.enclosure.audioUrl)

        audioPlayer.getPlayerStateListener()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                when (state) {
                    is AudioPlayerState.Prepare -> {
                        initSeekBarMax(state.maxDuration)
                    }

                    is AudioPlayerState.Playing -> {
                        updateSeekBarProgress(state.position)
                    }

                    is AudioPlayerState.Finished -> {
                        //TODO: Next Audio
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
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}