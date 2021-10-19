package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.data.remote.adapter.FlowCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

val networkModule = module {
    single(named(NetworkService.GENERAL_OKHTTP_CLIENT)) { NetworkService.provideGeneralOkHttpClient() }
    single(named(NetworkService.LOGGING_OKHTTP_CLIENT)) { NetworkService.provideLoggingOkHttpClient() }
    single { NetworkService.provideRetrofit(get(named(NetworkService.LOGGING_OKHTTP_CLIENT))) }
    single { NetworkService.provideApiService(get()) }
}

object NetworkService {
    private const val DOMAIN_URL = "https://feeds.soundcloud.com/"

    const val GENERAL_OKHTTP_CLIENT = "general_okhttp_client"
    const val LOGGING_OKHTTP_CLIENT = "logging_okhttp_client"

    fun provideLoggingOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    fun provideGeneralOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl(DOMAIN_URL)
            .client(okHttpClient)
            .build()
    }

    fun provideApiService(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}