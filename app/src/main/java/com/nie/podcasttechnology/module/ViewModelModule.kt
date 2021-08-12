package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.ui.detail.PodcastDetailViewModel
import com.nie.podcasttechnology.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { PodcastDetailViewModel() }
}