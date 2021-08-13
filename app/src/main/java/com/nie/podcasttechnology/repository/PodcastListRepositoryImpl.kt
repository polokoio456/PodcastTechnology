package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.data.remote.model.Rss
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PodcastListRepositoryImpl(private val api: Api) : PodcastListRepository {

    override fun fetchPodcasts(): Single<Rss> {
        return api.fetchPodcasts()
            .subscribeOn(Schedulers.io())
    }
}