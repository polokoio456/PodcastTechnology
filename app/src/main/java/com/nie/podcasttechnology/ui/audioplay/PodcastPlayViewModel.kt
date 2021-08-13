package com.nie.podcasttechnology.ui.audioplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import java.util.*

class PodcastPlayViewModel(private val databaseRepository: DatabaseRepository) : BaseViewModel() {

    private val _podcast = MutableLiveData<List<EntityPodcast>>()
    val podcast: LiveData<List<EntityPodcast>> = _podcast

    fun getNextPodcast(pubDate: Date) {
        databaseRepository.getNextPodcast(pubDate)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _podcast.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }
}