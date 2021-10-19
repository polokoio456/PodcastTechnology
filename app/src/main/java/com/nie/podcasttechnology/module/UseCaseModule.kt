package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.domain.EpisodeListUseCase
import com.nie.podcasttechnology.domain.EpisodePlayerUseCase
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
val useCaseModule = module {
    single { EpisodeListUseCase(get(), get()) }
    single { EpisodePlayerUseCase(get()) }
}