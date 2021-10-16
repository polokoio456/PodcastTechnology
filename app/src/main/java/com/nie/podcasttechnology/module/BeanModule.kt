package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.bean.AudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BeanModule {

    @Singleton
    @Provides
    fun provideAudioPlayer(): AudioPlayer {
        return AudioPlayer()
    }
}