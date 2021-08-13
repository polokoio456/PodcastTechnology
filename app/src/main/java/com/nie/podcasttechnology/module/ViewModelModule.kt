package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.ui.audioplay.EpisodePlayerViewModel
import com.nie.podcasttechnology.ui.main.EpisodeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EpisodeListViewModel(get(), get()) }
    viewModel { EpisodePlayerViewModel(get()) }
}