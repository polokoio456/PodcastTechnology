package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.data.remote.NetworkService
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.DatabaseRepositoryImpl
import com.nie.podcasttechnology.repository.EpisodeListRepository
import com.nie.podcasttechnology.repository.EpisodeListRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<EpisodeListRepository> { EpisodeListRepositoryImpl(NetworkService().create(Api::class.java)) }
    single<DatabaseRepository> { DatabaseRepositoryImpl(get(), get()) }
}