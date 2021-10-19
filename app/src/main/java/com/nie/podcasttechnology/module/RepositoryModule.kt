package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.DatabaseRepositoryImpl
import com.nie.podcasttechnology.repository.EpisodeListRepository
import com.nie.podcasttechnology.repository.EpisodeListRepositoryImpl
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
val repositoryModule = module {
    single<DatabaseRepository> { DatabaseRepositoryImpl(get(), get()) }
    single<EpisodeListRepository> { EpisodeListRepositoryImpl(get()) }
}