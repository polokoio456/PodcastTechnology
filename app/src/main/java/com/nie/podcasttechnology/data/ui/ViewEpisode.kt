package com.nie.podcasttechnology.data.ui

import java.io.Serializable
import java.util.*

data class ViewEpisode(
    val pubDate: Date,
    val title: String,
    val description: String,
    val type: String,
    val audioUrl: String,
    val imageUrl: String
) : Serializable