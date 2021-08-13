package com.nie.podcasttechnology.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import com.nie.podcasttechnology.data.database.dao.PodcastDao
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

class DatabaseRepositoryImpl(
    private val database: PodcastTechnologyDatabase,
    private val podcastDao: PodcastDao
) : DatabaseRepository {

    override fun clearAllDatabaseTables(): Single<Boolean> {
        return Single.create<Boolean> {
            database.clearAllTables()
            it.onSuccess(true)
        }.subscribeOn(Schedulers.io())
    }

    override fun insertPodcasts(podcasts: List<PodcastItem>): Completable {
        return podcastDao.insertPodcasts(podcasts.map { EntityPodcast.from(it) })
            .subscribeOn(Schedulers.io())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun listenPodcastsByDatePaging(): Flowable<PagingData<EntityPodcast>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 10),
            pagingSourceFactory = { podcastDao.listenPodcastsByDate() }
        ).flowable
    }

    override fun getNextPodcast(pubDate: Date): Single<List<EntityPodcast>> {
        return podcastDao.getNextPodcast(pubDate)
            .subscribeOn(Schedulers.io())
    }
}