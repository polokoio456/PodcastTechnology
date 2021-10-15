package com.nie.podcasttechnology.repository

import android.util.Log
import android.view.View
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import com.nie.podcasttechnology.data.database.dao.EpisodeDao
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.data.remote.model.EpisodeItem
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@FlowPreview
class DatabaseRepositoryImpl @Inject constructor(
    private val database: PodcastTechnologyDatabase,
    private val episodeDao: EpisodeDao
) : DatabaseRepository {

    override fun clearAllDatabaseTables() = flow {
        database.clearAllTables()
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun insertEpisodes(episodes: List<EpisodeItem>) = flow {
        episodeDao.insertEpisodes(episodes.map { EntityEpisode.from(it) })
        emit(true)
    }.flowOn(Dispatchers.IO)

    override fun listenEpisodesByDatePaging(): Flow<PagingData<ViewEpisode>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 10),
            pagingSourceFactory = { episodeDao.listenEpisodesByDate() }
        ).flow.flowOn(Dispatchers.IO)
            .flatMapMerge {
                flow { emit(it.map { entity -> entity.toViewEpisode() }) }
            }.flowOn(Dispatchers.IO)
    }

    override fun getNextEpisode(pubDate: Date): Flow<List<ViewEpisode>> {
        return episodeDao.getNextEpisode(pubDate)
            .flowOn(Dispatchers.IO)
            .flatMapMerge {
                flow { emit(it.map { entity -> entity.toViewEpisode() }) }
            }.flowOn(Dispatchers.IO)
    }

    override fun getLatestEpisode(): Flow<List<ViewEpisode>> {
        return episodeDao.getLatestEpisode()
            .flowOn(Dispatchers.IO)
            .flatMapMerge {
                flow { emit(it.map { entity -> entity.toViewEpisode() }) }
            }.flowOn(Dispatchers.IO)
    }
}