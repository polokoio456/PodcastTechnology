package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.data.remote.model.Rss
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EpisodeListRepositoryImpl @Inject constructor(private val api: Api) : EpisodeListRepository {

    override fun fetchEpisodes(): Single<Rss> {
        return api.fetchEpisodes()
            .subscribeOn(Schedulers.io())
    }
}