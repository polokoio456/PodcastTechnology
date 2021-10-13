package com.nie.podcasttechnology.ui.main.module

import com.nie.podcasttechnology.ui.main.EpisodeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class EpisodeListActivityModule {

    @Provides
    fun provideEpisodeAdapter() : EpisodeAdapter {
        return EpisodeAdapter()
    }
}