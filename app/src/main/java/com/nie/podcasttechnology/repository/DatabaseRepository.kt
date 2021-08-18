package com.nie.podcasttechnology.repository

import androidx.paging.PagingData
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.data.remote.model.EpisodeItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface DatabaseRepository {
    fun clearAllDatabaseTables(): Single<Boolean>
    fun insertEpisodes(episodes: List<EpisodeItem>): Completable
    fun listenEpisodesByDatePaging(): Flowable<PagingData<EntityEpisode>>
    fun getNextEpisode(pubDate: Date): Single<List<EntityEpisode>>
    fun getLatestEpisode(): Single<List<EntityEpisode>>
}