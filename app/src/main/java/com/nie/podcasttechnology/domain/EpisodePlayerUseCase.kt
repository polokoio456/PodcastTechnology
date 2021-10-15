package com.nie.podcasttechnology.domain

import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class EpisodePlayerUseCase(
    private val databaseRepository: DatabaseRepository
) {
    fun getNextEpisode(pubDate: Date): Flow<List<ViewEpisode>> {
        return databaseRepository.getNextEpisode(pubDate)
    }

    fun getLatestEpisode(): Flow<List<ViewEpisode>> {
        return databaseRepository.getLatestEpisode()
    }
}