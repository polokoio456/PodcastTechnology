package com.nie.podcasttechnology.data.database

import androidx.room.Database
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
}