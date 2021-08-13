package com.nie.podcasttechnology.ui.audioplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import java.util.*

class EpisodePlayerViewModel(private val databaseRepository: DatabaseRepository) : BaseViewModel() {

    private val _episode = MutableLiveData<List<EntityEpisode>>()
    val episode: LiveData<List<EntityEpisode>> = _episode

    fun getNextPodcast(pubDate: Date) {
        databaseRepository.getNextEpisode(pubDate)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _episode.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }
}