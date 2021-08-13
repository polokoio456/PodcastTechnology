package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.model.Rss
import io.reactivex.Single

interface EpisodeListRepository {
    fun fetchEpisodes(): Single<Rss>
}