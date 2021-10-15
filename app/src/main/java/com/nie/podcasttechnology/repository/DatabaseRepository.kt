package com.nie.podcasttechnology.repository

import androidx.paging.PagingData
import com.nie.podcasttechnology.data.remote.model.EpisodeItem
import com.nie.podcasttechnology.data.ui.ViewEpisode
import kotlinx.coroutines.flow.Flow
import java.util.*

interface DatabaseRepository {
    fun clearAllDatabaseTables(): Flow<Boolean>
    fun insertEpisodes(episodes: List<EpisodeItem>): Flow<Boolean>
    fun listenEpisodesByDatePaging(): Flow<PagingData<ViewEpisode>>
    fun getNextEpisode(pubDate: Date): Flow<List<ViewEpisode>>
    fun getLatestEpisode(): Flow<List<ViewEpisode>>
}