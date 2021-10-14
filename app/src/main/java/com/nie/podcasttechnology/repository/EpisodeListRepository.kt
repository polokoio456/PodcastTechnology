package com.nie.podcasttechnology.repository

import com.nie.podcasttechnology.data.remote.model.Rss
import kotlinx.coroutines.flow.Flow

interface EpisodeListRepository {
    fun fetchEpisodes(): Flow<Rss>
}