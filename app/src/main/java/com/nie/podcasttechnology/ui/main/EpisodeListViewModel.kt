package com.nie.podcasttechnology.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.domain.EpisodeListUseCase
import com.nie.podcasttechnology.util.Constant
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
class EpisodeListViewModel (
    private val episodeListUseCase: EpisodeListUseCase
) : BaseViewModel() {

    private val _episodes = MutableLiveData<PagingData<ViewEpisode>>()
    val episodes: LiveData<PagingData<ViewEpisode>> = _episodes

    private val _coverImageUrl = MutableLiveData<String>()
    val coverImageUrl: LiveData<String> = _coverImageUrl

    private val _serverError = MutableLiveData<Boolean>()
    val serverError: LiveData<Boolean> = _serverError

    fun fetchEpisodes() {
        viewModelScope.launch {
            episodeListUseCase.fetchEpisodes()
                .catch { e ->
                    _serverError.value = true
                    Log.e(Constant.TAG, e.stackTraceToString())
                }
                .collect {
                    _coverImageUrl.value = it
                }
        }
    }

    fun listenEpisodes() {
        viewModelScope.launch {
            episodeListUseCase.listenEpisodesByDatePaging()
                .catch { e -> Log.e(Constant.TAG, e.stackTraceToString()) }
                .collect {
                    _episodes.value = it
                }
        }
    }
}