package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.ui.audioplay.EpisodePlayerViewModel
import com.nie.podcasttechnology.ui.main.EpisodeListViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
val viewModelModule = module {
    viewModel { EpisodeListViewModel(get()) }
    viewModel { EpisodePlayerViewModel(get()) }
}