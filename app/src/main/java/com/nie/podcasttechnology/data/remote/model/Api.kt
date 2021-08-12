package com.nie.podcasttechnology.data.remote.model

import io.reactivex.Single
import retrofit2.http.GET

interface Api {
    @GET("users/soundcloud:users:322164009/sounds.rss")
    fun fetchPodcasts(): Single<Rss>
}