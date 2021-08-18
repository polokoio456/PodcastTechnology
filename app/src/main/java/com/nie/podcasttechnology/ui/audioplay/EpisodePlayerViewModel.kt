package com.nie.podcasttechnology.ui.audioplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import java.util.*

class EpisodePlayerViewModel(private val databaseRepository: DatabaseRepository) : BaseViewModel() {

    private val _episode = MutableLiveData<List<ViewEpisode>>()
    val episode: LiveData<List<ViewEpisode>> = _episode

    private val _latestEpisode = MutableLiveData<ViewEpisode>()
    val latestEpisode: LiveData<ViewEpisode> = _latestEpisode

    fun getNextPodcast(pubDate: Date) {
        databaseRepository.getNextEpisode(pubDate)
            .map {
                it.map { entity ->
                    entity.toViewEpisode()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _episode.value = it
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }

    fun getLatestEpisode() {
        databaseRepository.getLatestEpisode()
            .map { it.map { entity -> entity.toViewEpisode() } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _latestEpisode.value = it.first()
            }, {
                Log.e(Constant.TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }
}