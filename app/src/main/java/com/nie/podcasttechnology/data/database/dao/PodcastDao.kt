package com.nie.podcasttechnology.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPodcasts(entities: List<EntityPodcast>): Completable

    @Transaction
    @Query("SELECT * FROM Podcasts ORDER BY pubDate DESC")
    fun listenPodcastsByDate(): PagingSource<Int, EntityPodcast>

    @Query("SELECT * FROM Podcasts WHERE pubDate > :pubDate ORDER BY pubDate ASC LIMIT 1")
    fun getNextPodcast(pubDate: Date): Single<List<EntityPodcast>>

    @Query("DELETE FROM Podcasts")
    fun nukeTable(): Single<Int>
}