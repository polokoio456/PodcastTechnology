package com.nie.podcasttechnology.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.databinding.ActivityMainBinding
import com.nie.podcasttechnology.ui.detail.EpisodeDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import org.jetbrains.anko.toast
import javax.inject.Inject

@FlowPreview
@AndroidEntryPoint
class EpisodeListActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val viewModel by viewModels<EpisodeListViewModel>()

    @Inject
    lateinit var adapter: EpisodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.listenEpisodes()
        viewModel.fetchEpisodes()

        initView()
        setOnClickListener()
        observableLiveData()
    }

    override fun onDestroy() {
        adapter.clear()
        super.onDestroy()
    }

    private fun initView() {
        binding.recyclerViewEpisodes.isNestedScrollingEnabled = false
        binding.recyclerViewEpisodes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEpisodes.adapter = adapter
    }

    private fun setOnClickListener() {
        adapter.onItemClicked = {
            EpisodeDetailActivity.start(this, it)
        }
    }

    private fun observableLiveData() {
        viewModel.coverImageUrl.observe(this) {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.place_holder_grey)
                .into(binding.imagePodcastCover)
        }

        viewModel.episodes.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        viewModel.serverError.observe(this) {
            toast(R.string.server_error)
        }
    }
}