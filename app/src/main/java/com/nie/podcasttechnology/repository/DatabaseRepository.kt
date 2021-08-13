package com.nie.podcasttechnology.repository

import androidx.paging.PagingData
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface DatabaseRepository {
    fun clearAllDatabaseTables(): Single<Boolean>
    fun insertPodcasts(podcasts: List<PodcastItem>): Completable
    fun listenPodcastsByDatePaging(): Flowable<PagingData<EntityPodcast>>
    fun getNextPodcast(pubDate: Date): Single<List<EntityPodcast>>
}