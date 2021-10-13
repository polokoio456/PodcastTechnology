package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import com.nie.podcasttechnology.data.database.dao.EpisodeDao
import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.DatabaseRepositoryImpl
import com.nie.podcasttechnology.repository.EpisodeListRepository
import com.nie.podcasttechnology.repository.EpisodeListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDatabaseRepository(databaseTechnologyDatabase: PodcastTechnologyDatabase, episodeDao: EpisodeDao): DatabaseRepository {
        return DatabaseRepositoryImpl(databaseTechnologyDatabase, episodeDao)
    }

    @Singleton
    @Provides
    fun provideEpisodeListRepository(api: Api): EpisodeListRepository {
        return EpisodeListRepositoryImpl(api)
    }
}