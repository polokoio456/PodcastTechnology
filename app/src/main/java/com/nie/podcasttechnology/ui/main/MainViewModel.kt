package com.nie.podcasttechnology.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.MainRepository
import com.nie.podcasttechnology.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo

class MainViewModel(
    private val mainRepository: MainRepository,
    private val databaseRepository: DatabaseRepository
) : BaseViewModel() {

    private val _podcasts = MutableLiveData<List<EntityPodcast>>()
    val podcasts: LiveData<List<EntityPodcast>> = _podcasts

    private val _coverImageUrl = MutableLiveData<String>()
    val coverImageUrl: LiveData<String> = _coverImageUrl

    fun fetchPodcasts() {
        mainRepository.fetchPodcasts()
            .doOnSuccess {
                _coverImageUrl.postValue(it.channel.image[0].imageUrl)
            }
            .flatMapCompletable { databaseRepository.insertPodcasts(it.channel.items) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
            .subscribe({ }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }

    fun listenPodcast() {
        databaseRepository.listenPodcastsByDate()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _podcasts.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }
}