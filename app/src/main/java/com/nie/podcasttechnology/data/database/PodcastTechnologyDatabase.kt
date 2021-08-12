package com.nie.podcasttechnology.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nie.podcasttechnology.data.database.converter.DateTypeConverter
import com.nie.podcasttechnology.data.database.dao.PodcastDao
import com.nie.podcasttechnology.data.database.model.EntityPodcast

@Database(
    exportSchema = false,
    entities = [
        EntityPodcast::class
    ],
    version = 1
)
@TypeConverters(
    DateTypeConverter::class
)
abstract class PodcastTechnologyDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao

    companion object {
        @Volatile
        private var INSTANCE: PodcastTechnologyDatabase? = null

        fun getInstance(context: Context): PodcastTechnologyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PodcastTechnologyDatabase::class.java, "podcast.db"
            ).build()
    }
}