package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.Api
import com.nie.podcasttechnology.data.remote.model.Rss
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class EpisodeListRepositoryImpl(
    private val api: Api
) : EpisodeListRepository {

    override fun fetchEpisodes(): Flow<Rss> {
        return api.fetchEpisodes()
            .flowOn(Dispatchers.IO)
    }
}