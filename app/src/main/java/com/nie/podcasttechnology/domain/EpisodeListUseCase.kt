package com.nie.podcasttechnology.domain

import androidx.paging.PagingData
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.EpisodeListRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

@FlowPreview
class EpisodeListUseCase(
    private val episodeListRepository: EpisodeListRepository,
    private val databaseRepository: DatabaseRepository
) {
    fun fetchEpisodes(): Flow<String> {
        var imageUrl = ""

        return databaseRepository.clearAllDatabaseTables()
            .flatMapMerge { episodeListRepository.fetchEpisodes() }
            .onEach { imageUrl = it.channel.image[0].imageUrl!! }
            .flatMapMerge { databaseRepository.insertEpisodes(it.channel.items) }
            .flatMapMerge { flowOf(imageUrl) }
    }

    fun listenEpisodesByDatePaging(): Flow<PagingData<EntityEpisode>> {
        return databaseRepository.listenEpisodesByDatePaging()
    }
}