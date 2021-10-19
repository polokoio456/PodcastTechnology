package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.remote.NetworkService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named(NetworkService.GENERAL_OKHTTP_CLIENT)) { NetworkService().provideGeneralOkHttpClient() }
    single(named(NetworkService.LOGGING_OKHTTP_CLIENT)) { NetworkService().provideLoggingOkHttpClient() }
    single { NetworkService().provideRetrofit(get(named(NetworkService.LOGGING_OKHTTP_CLIENT))) }
    single { NetworkService().provideApiService(get()) }
}