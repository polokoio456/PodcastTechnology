package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.data.remote.adapter.FlowCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeneralOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {
    private const val TIMEOUT_SECOND = 60L
    private const val DOMAIN_URL = "https://feeds.soundcloud.com/"

    @GeneralOkHttpClient
    @Singleton
    @Provides
    fun provideGeneralOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .build()
    }

    @LoggingOkHttpClient
    @Singleton
    @Provides
    fun provideLoggingOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @GeneralOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl(DOMAIN_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}