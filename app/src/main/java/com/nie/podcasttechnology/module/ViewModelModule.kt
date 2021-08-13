package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.ui.audioplay.PodcastPlayViewModel
import com.nie.podcasttechnology.ui.detail.PodcastDetailViewModel
import com.nie.podcasttechnology.ui.main.PodcastListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PodcastListViewModel(get(), get()) }
    viewModel { PodcastDetailViewModel() }
    viewModel { PodcastPlayViewModel(get()) }
}