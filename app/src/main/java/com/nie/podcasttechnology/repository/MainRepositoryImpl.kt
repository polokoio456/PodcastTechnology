package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.model.Api
import com.nie.podcasttechnology.data.remote.model.Rss
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MainRepositoryImpl(private val api: Api) : MainRepository {

    override fun fetchPodcasts(): Single<Rss> {
        return api.fetchPodcasts()
            .subscribeOn(Schedulers.io())
    }
}