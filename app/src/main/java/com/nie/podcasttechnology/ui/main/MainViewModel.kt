package com.nie.podcasttechnology.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.remote.model.Rss
import com.nie.podcasttechnology.repository.MainRepository
import com.nie.podcasttechnology.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    private val _rss = MutableLiveData<Rss>()
    val rss: LiveData<Rss> = _rss

    fun fetchPodcasts() {
        repository.fetchPodcasts()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doFinally { hideLoading() }
            .subscribe({
                _rss.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}