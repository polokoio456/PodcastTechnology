package com.nie.podcasttechnology.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nie.podcasttechnology.data.database.converter.DateTypeConverter
import com.nie.podcasttechnology.data.database.dao.EpisodeDao
import com.nie.podcasttechnology.data.database.model.EntityEpisode

@Database(
    exportSchema = false,
    entities = [
        EntityEpisode::class
    ],
    version = 1
)
@TypeConverters(
    DateTypeConverter::class
)
abstract class PodcastTechnologyDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao

    companion object {
        fun provideDatabase(context: Context): PodcastTechnologyDatabase {
            return Room.databaseBuilder(
                context,
                PodcastTechnologyDatabase::class.java, "podcast.db"
            ).build()
        }
    }
}