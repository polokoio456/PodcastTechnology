package com.nie.podcasttechnology.ui.audioplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.domain.EpisodePlayerUseCase
import com.nie.podcasttechnology.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EpisodePlayerViewModel @Inject constructor(
    private val episodePlayerUseCase: EpisodePlayerUseCase
) : BaseViewModel() {

    private val _episode = MutableLiveData<List<ViewEpisode>>()
    val episode: LiveData<List<ViewEpisode>> = _episode

    private val _latestEpisode = MutableLiveData<ViewEpisode>()
    val latestEpisode: LiveData<ViewEpisode> = _latestEpisode

    fun getNextPodcast(pubDate: Date) {
        viewModelScope.launch {
            episodePlayerUseCase.getNextEpisode(pubDate)
                .catch { e -> Log.e(Constant.TAG, e.stackTraceToString()) }
                .collect {
                    _episode.value = it
                }
        }
    }

    fun getLatestEpisode() {
        viewModelScope.launch {
            episodePlayerUseCase.getLatestEpisode()
                .catch { e -> Log.e(Constant.TAG, e.stackTraceToString()) }
                .collect {
                    _latestEpisode.value = it.first()
                }
        }
    }
}