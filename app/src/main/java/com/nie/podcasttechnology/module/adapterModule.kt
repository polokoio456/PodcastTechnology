package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.ui.main.PodcastAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory { PodcastAdapter() }
}