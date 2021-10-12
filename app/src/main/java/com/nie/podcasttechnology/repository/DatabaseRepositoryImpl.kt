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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val database: PodcastTechnologyDatabase,
    private val episodeDao: EpisodeDao
) : DatabaseRepository {

    override fun clearAllDatabaseTables(): Single<Boolean> {
        return Single.create<Boolean> {
            database.clearAllTables()
            it.onSuccess(true)
        }.subscribeOn(Schedulers.io())
    }

    override fun insertEpisodes(episodes: List<EpisodeItem>): Completable {
        return episodeDao.insertEpisodes(episodes.map { EntityEpisode.from(it) })
            .subscribeOn(Schedulers.io())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun listenEpisodesByDatePaging(): Flowable<PagingData<EntityEpisode>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 10),
            pagingSourceFactory = { episodeDao.listenEpisodesByDate() }
        ).flowable
    }

    override fun getNextEpisode(pubDate: Date): Single<List<EntityEpisode>> {
        return episodeDao.getNextEpisode(pubDate)
            .subscribeOn(Schedulers.io())
    }

    override fun getLatestEpisode(): Single<List<EntityEpisode>> {
        return episodeDao.getLatestEpisode()
            .subscribeOn(Schedulers.io())
    }
}