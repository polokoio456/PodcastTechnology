package com.nie.podcasttechnology.repository

import androidx.paging.PagingData
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.data.remote.model.EpisodeItem
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DatabaseRepository {
    fun clearAllDatabaseTables(): Flow<Boolean>
    fun insertEpisodes(episodes: List<EpisodeItem>): Flow<Boolean>
    fun listenEpisodesByDatePaging(): Flow<PagingData<EntityEpisode>>
    fun getNextEpisode(pubDate: Date): Flow<List<EntityEpisode>>
    fun getLatestEpisode(): Flow<List<EntityEpisode>>
}