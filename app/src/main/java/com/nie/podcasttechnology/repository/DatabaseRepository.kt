package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import io.reactivex.Completable
import io.reactivex.Flowable

interface DatabaseRepository {
    fun insertPodcasts(podcasts: List<PodcastItem>): Completable
    fun listenPodcastsByDate(): Flowable<List<EntityPodcast>>
}