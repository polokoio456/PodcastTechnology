package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import com.nie.podcasttechnology.data.database.dao.PodcastDao
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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

    override fun listenPodcastsByDate(): Flowable<List<EntityPodcast>> {
        return podcastDao.listenPodcastsByDate()
            .subscribeOn(Schedulers.io())
    }

    override fun getNextPodcast(pubDate: Date): Single<List<EntityPodcast>> {
        return podcastDao.getNextPodcast(pubDate)
            .subscribeOn(Schedulers.io())
    }
}