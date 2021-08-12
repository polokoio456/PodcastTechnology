package com.nie.podcasttechnology.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import com.nie.podcasttechnology.databinding.ActivityPodcastDetailBinding
import com.nie.podcasttechnology.extension.throttleClick
import com.nie.podcasttechnology.ui.audioplay.AudioPlayActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class PodcastDetailActivity : BaseActivity() {

    companion object {
        private const val KEY_PODCAST_ITEM = "key_podcast_item"

        fun start(activity: Activity, item: EntityPodcast) {
            Intent(activity, PodcastDetailActivity::class.java).apply {
                putExtra(KEY_PODCAST_ITEM, item)
            }.let { activity.startActivity(it) }
        }
    }

    private val binding by lazy { ActivityPodcastDetailBinding.inflate(layoutInflater) }

    override val viewModel by viewModel<PodcastDetailViewModel>()

    private val podcastItem by lazy { intent.getSerializableExtra(KEY_PODCAST_ITEM) as EntityPodcast }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        setOnClickListener()
    }

    private fun initView() {
        Glide.with(this)
            .load(podcastItem.imageUrl)
            .placeholder(R.drawable.place_holder_grey)
            .into(binding.imagePodcastCover)

        binding.textSubtitle.text = podcastItem.title
        binding.textDescription.text = podcastItem.description
    }

    private fun setOnClickListener() {
        RxView.clicks(binding.imagePlayAudio)
            .throttleClick()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                AudioPlayActivity.start(this, podcastItem)
            }.addTo(compositeDisposable)
    }
}