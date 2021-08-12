package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.database.dao.PodcastDao
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class DatabaseRepositoryImpl(private val podcastDao: PodcastDao) : DatabaseRepository {

    override fun insertPodcasts(podcasts: List<PodcastItem>): Completable {
        return podcastDao.insertPodcasts(podcasts.map { EntityPodcast.from(it) })
            .subscribeOn(Schedulers.io())
    }

    override fun listenPodcastsByDate(): Flowable<List<EntityPodcast>> {
        return podcastDao.listenPodcastsByDate()
            .subscribeOn(Schedulers.io())
    }
}