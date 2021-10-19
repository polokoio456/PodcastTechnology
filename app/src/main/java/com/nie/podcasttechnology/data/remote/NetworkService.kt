package com.nie.podcasttechnology.data.remote

import com.nie.podcasttechnology.data.remote.adapter.FlowCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class NetworkService {
    companion object {
        private const val DOMAIN_URL = "https://feeds.soundcloud.com/"

        const val GENERAL_OKHTTP_CLIENT = "general_okhttp_client"
        const val LOGGING_OKHTTP_CLIENT = "logging_okhttp_client"
    }

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