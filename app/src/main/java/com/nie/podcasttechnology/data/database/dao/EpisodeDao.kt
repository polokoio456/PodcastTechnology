package com.nie.podcasttechnology.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(entities: List<EntityEpisode>): Completable

    @Transaction
    @Query("SELECT * FROM Episodes ORDER BY pubDate DESC")
    fun listenEpisodesByDate(): PagingSource<Int, EntityEpisode>

    @Query("SELECT * FROM Episodes WHERE pubDate > :pubDate ORDER BY pubDate ASC LIMIT 1")
    fun getNextEpisode(pubDate: Date): Single<List<EntityEpisode>>

    @Query("DELETE FROM Episodes")
    fun nukeTable(): Single<Int>
}