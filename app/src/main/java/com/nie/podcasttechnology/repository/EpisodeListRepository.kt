package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.model.Rss
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface EpisodeListRepository {
    fun fetchEpisodes(): Flow<Rss>
}