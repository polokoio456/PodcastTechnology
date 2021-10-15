package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.domain.EpisodeListUseCase
import com.nie.podcasttechnology.domain.EpisodePlayerUseCase
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.EpisodeListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideEpisodeListUseCase(databaseRepository: DatabaseRepository, episodeListRepository: EpisodeListRepository): EpisodeListUseCase {
        return EpisodeListUseCase(episodeListRepository, databaseRepository)
    }

    @Singleton
    @Provides
    fun provideEpisodePlayerUseCase(databaseRepository: DatabaseRepository): EpisodePlayerUseCase {
        return EpisodePlayerUseCase(databaseRepository)
    }
}