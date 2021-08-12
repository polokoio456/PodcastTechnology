package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.remote.model.Api
import com.nie.podcasttechnology.data.remote.model.NetworkService
import com.nie.podcasttechnology.repository.MainRepository
import com.nie.podcasttechnology.repository.MainRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MainRepository> { MainRepositoryImpl(NetworkService().create(Api::class.java)) }
}