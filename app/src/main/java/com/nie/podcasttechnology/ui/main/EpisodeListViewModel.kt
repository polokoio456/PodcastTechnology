package com.nie.podcasttechnology.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.database.model.EntityEpisode
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

    private val _episodes = MutableLiveData<PagingData<EntityEpisode>>()
    val episodes: LiveData<PagingData<EntityEpisode>> = _episodes

    private val _coverImageUrl = MutableLiveData<String>()
    val coverImageUrl: LiveData<String> = _coverImageUrl

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
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun listenEpisodes() {
        databaseRepository.listenEpisodesByDatePaging()
            .cachedIn(viewModelScope)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _episodes.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }
}