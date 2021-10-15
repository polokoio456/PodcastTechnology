package com.nie.podcasttechnology.ui.audioplay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.bean.AudioPlayer
import com.nie.podcasttechnology.bean.AudioPlayerState
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.databinding.ActivityAudioPlayBinding
import com.nie.podcasttechnology.extension.addTo
import com.nie.podcasttechnology.extension.throttleClick
import com.nie.podcasttechnology.extension.toFormatTimeStr
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@AndroidEntryPoint
class EpisodePlayerActivity : BaseActivity() {

    companion object {
        private const val KEY_PODCAST_ITEM = "key_podcast_item"

        fun start(activity: Activity, item: ViewEpisode) {
            Intent(activity, EpisodePlayerActivity::class.java).apply {
                putExtra(KEY_PODCAST_ITEM, item)
            }.let { activity.startActivity(it) }
        }
    }

    private val binding by lazy { ActivityAudioPlayBinding.inflate(layoutInflater) }

    override val viewModel by viewModels<EpisodePlayerViewModel>()

    private val podcastItem by lazy { intent.getSerializableExtra(KEY_PODCAST_ITEM) as ViewEpisode }

    @Inject
    lateinit var audioPlayer: AudioPlayer

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
        lifecycle.removeObserver(audioPlayer)
        super.onDestroy()
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
                        setSeekBarMax(state.maxDuration)
                        setMaxDurationText(state.maxDuration / 1000)
                    }

                    is AudioPlayerState.Playing -> {
                        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_circle_filled_24)
                        binding.imageAudioController.setImageDrawable(drawable)
                        updateSeekBarAndTimeText()
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

    private fun initSeekBarListener() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioPlayer.playerSeekTo(progress)
                    updateSeekBarAndTimeText()
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
                updateSeekBarAndTimeText()
            }.addTo(compositeDisposable)

        RxView.clicks(binding.imageRewind)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                audioPlayer.rewind()
                updateSeekBarAndTimeText()
            }.addTo(compositeDisposable)
    }

    private fun observableLiveData() {
        viewModel.episode.observe(this, {
            if (it.isEmpty()) {
                viewModel.getLatestEpisode()
                return@observe
            }

            val episode = it.first()
            binding.textTitle.text = episode.title
            audioPlayer.resetPlayer(this, episode.pubDate, episode.audioUrl)
        })

        viewModel.latestEpisode.observe(this, {
            audioPlayer.resetPlayer(this, it.pubDate, it.audioUrl, false)
        })
    }

    private fun setMaxDurationText(maxDuration: Int) {
        binding.textMaxDuration.text = maxDuration.toFormatTimeStr()
    }

    private fun setCurrentDurationText(currentPosition: Int) {
        val str = "${currentPosition.toFormatTimeStr()} / "
        binding.textCurrentDuration.text = str
    }

    private fun setSeekBarMax(duration: Int) {
        binding.seekBar.max = duration
    }

    private fun updateSeekBarProgress(currentPosition: Int) {
        binding.seekBar.progress = currentPosition
    }

    private fun updateSecondaryProgress(progress: Int) {
        binding.seekBar.secondaryProgress = progress
    }

    private fun updateSeekBarAndTimeText() {
        val currentPosition = audioPlayer.getCurrentPosition()!!
        updateSeekBarProgress(currentPosition)
        setCurrentDurationText(currentPosition / 1000)
    }
}