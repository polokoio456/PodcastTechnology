package com.nie.podcasttechnology.ui.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.databinding.ActivityMainBinding
import com.nie.podcasttechnology.ui.detail.PodcastDetailActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PodcastListActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val viewModel by viewModel<PodcastListViewModel>()

    private val adapter by inject<PodcastAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.listenPodcast()
        viewModel.fetchPodcasts()

        initView()
        setOnClickListener()
        observableLiveData()
    }

    override fun onDestroy() {
        adapter.clear()
        super.onDestroy()
    }

    private fun initView() {
        binding.recyclerViewPodcasts.isNestedScrollingEnabled = false
        binding.recyclerViewPodcasts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPodcasts.adapter = adapter
    }

    private fun setOnClickListener() {
        adapter.onItemClicked = {
            PodcastDetailActivity.start(this, it)
        }
    }

    private fun observableLiveData() {
        viewModel.coverImageUrl.observe(this, {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.place_holder_grey)
                .into(binding.imagePodcastCover)
        })

        viewModel.podcasts.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }
}