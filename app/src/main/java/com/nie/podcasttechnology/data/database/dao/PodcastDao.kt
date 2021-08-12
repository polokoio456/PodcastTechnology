package com.nie.podcasttechnology.data.database.dao

import androidx.room.*
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPodcasts(entities: List<EntityPodcast>): Completable

    @Transaction
    @Query("SELECT * FROM Podcasts ORDER BY pubDate DESC")
    fun listenPodcastsByDate(): Flowable<List<EntityPodcast>>
}