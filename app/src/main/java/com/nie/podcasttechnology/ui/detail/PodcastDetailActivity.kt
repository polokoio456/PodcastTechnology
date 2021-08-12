package com.nie.podcasttechnology.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import com.nie.podcasttechnology.databinding.ActivityPodcastDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PodcastDetailActivity : BaseActivity() {

    companion object {
        private const val KEY_PODCAST_ITEM = "key_podcast_item"

        fun start(activity: Activity, item: PodcastItem) {
            Intent(activity, PodcastDetailActivity::class.java).apply {
                putExtra(KEY_PODCAST_ITEM, item)
            }.let { activity.startActivity(it) }
        }
    }

    private val binding by lazy { ActivityPodcastDetailBinding.inflate(layoutInflater) }

    override val viewModel by viewModel<PodcastDetailViewModel>()

    private val podcastItem by lazy { intent.getSerializableExtra(KEY_PODCAST_ITEM) as PodcastItem }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        Glide.with(this)
            .load(podcastItem.image.imageUrl)
            .placeholder(R.drawable.place_holder_grey)
            .into(binding.imagePodcastCover)

        binding.textSubtitle.text = podcastItem.title
        binding.textDescription.text = podcastItem.description
    }
}