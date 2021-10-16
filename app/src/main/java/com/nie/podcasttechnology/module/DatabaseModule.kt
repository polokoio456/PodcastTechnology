package com.nie.podcasttechnology.module

import android.content.Context
import androidx.room.Room
import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import com.nie.podcasttechnology.data.database.dao.EpisodeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PodcastTechnologyDatabase {
        return Room.databaseBuilder(
            context,
            PodcastTechnologyDatabase::class.java, "podcast.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideEpisodeDao(podcastTechnologyDatabase: PodcastTechnologyDatabase): EpisodeDao {
        return podcastTechnologyDatabase.episodeDao()
    }
}