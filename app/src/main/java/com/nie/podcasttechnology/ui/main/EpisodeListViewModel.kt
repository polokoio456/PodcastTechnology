package com.nie.podcasttechnology.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.EpisodeListRepository
import com.nie.podcasttechnology.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class EpisodeListViewModel @Inject constructor(
    private val episodeListRepository: EpisodeListRepository,
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    private val _episodes = MutableLiveData<PagingData<ViewEpisode>>()
    val episodes: LiveData<PagingData<ViewEpisode>> = _episodes

    private val _coverImageUrl = MutableLiveData<String>()
    val coverImageUrl: LiveData<String> = _coverImageUrl

    private val _serverError = MutableLiveData<Boolean>()
    val serverError: LiveData<Boolean> = _serverError

    fun fetchEpisodes() {
        viewModelScope.launch {
            databaseRepository.clearAllDatabaseTables()
                .flatMapMerge { episodeListRepository.fetchEpisodes() }
                .onEach { _coverImageUrl.postValue(it.channel.image[0].imageUrl!!) }
                .flatMapMerge { databaseRepository.insertEpisodes(it.channel.items) }
                .catch { e ->
                    _serverError.value = true
                    Log.e(Constant.TAG, e.stackTraceToString())
                }
                .collect()
        }
    }

    fun listenEpisodes() {
        viewModelScope.launch {
            databaseRepository.listenEpisodesByDatePaging()
                .map { it.map { entity -> entity.toViewEpisode() } }
                .catch { e ->
                    Log.e(Constant.TAG, e.stackTraceToString())
                }
                .collect {
                    _episodes.value = it
                }
        }
    }
}