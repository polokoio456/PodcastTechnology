package com.nie.podcasttechnology.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import com.nie.podcasttechnology.data.database.dao.EpisodeDao
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.data.remote.model.EpisodeItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import java.util.*
import javax.inject.Inject

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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun listenEpisodesByDatePaging(): Flow<PagingData<EntityEpisode>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 10),
            pagingSourceFactory = { episodeDao.listenEpisodesByDate() }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun getNextEpisode(pubDate: Date): Flow<List<EntityEpisode>> {
        return episodeDao.getNextEpisode(pubDate)
            .flowOn(Dispatchers.IO)
    }

    override fun getLatestEpisode(): Flow<List<EntityEpisode>> {
        return episodeDao.getLatestEpisode()
            .flowOn(Dispatchers.IO)
    }
}