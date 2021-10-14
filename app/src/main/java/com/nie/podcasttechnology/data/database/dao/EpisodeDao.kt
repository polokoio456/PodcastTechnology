package com.nie.podcasttechnology.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(entities: List<EntityEpisode>)

    @Transaction
    @Query("SELECT * FROM Episodes ORDER BY pubDate DESC")
    fun listenEpisodesByDate(): PagingSource<Int, EntityEpisode>

    @Query("SELECT * FROM Episodes WHERE pubDate > :pubDate ORDER BY pubDate ASC LIMIT 1")
    fun getNextEpisode(pubDate: Date): Flow<List<EntityEpisode>>

    @Query("SELECT * FROM Episodes ORDER BY pubDate DESC LIMIT 1")
    fun getLatestEpisode(): Flow<List<EntityEpisode>>
}