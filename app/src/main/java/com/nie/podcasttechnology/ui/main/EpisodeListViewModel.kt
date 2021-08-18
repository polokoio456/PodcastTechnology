package com.nie.podcasttechnology.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.EpisodeListRepository
import com.nie.podcasttechnology.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.ExperimentalCoroutinesApi

class EpisodeListViewModel(
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
        databaseRepository.clearAllDatabaseTables()
            .flatMap { episodeListRepository.fetchEpisodes() }
            .doOnSuccess {
                _coverImageUrl.postValue(it.channel.image[0].imageUrl!!)
            }
            .flatMapCompletable { databaseRepository.insertEpisodes(it.channel.items) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
            .subscribe({ }, {
                _serverError.value = true
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun listenEpisodes() {
        databaseRepository.listenEpisodesByDatePaging()
            .cachedIn(viewModelScope)
            .map {
                it.map { entity ->
                    entity.toViewEpisode()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _episodes.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }
}