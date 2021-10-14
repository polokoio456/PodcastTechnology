package com.nie.podcasttechnology.data.remote

import com.nie.podcasttechnology.data.remote.model.Rss
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface Api {
    @GET("users/soundcloud:users:322164009/sounds.rss")
    fun fetchEpisodes(): Flow<Rss>
}