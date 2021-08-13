package com.nie.podcasttechnology.data.database.dao

import androidx.room.*
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPodcasts(entities: List<EntityPodcast>): Completable

    @Transaction
    @Query("SELECT * FROM Podcasts ORDER BY pubDate DESC")
    fun listenPodcastsByDate(): Flowable<List<EntityPodcast>>

    @Query("SELECT * FROM Podcasts WHERE pubDate > :pubDate ORDER BY pubDate ASC LIMIT 1")
    fun getNextPodcast(pubDate: Date): Single<List<EntityPodcast>>

    @Query("DELETE FROM Podcasts")
    fun nukeTable(): Single<Int>
}