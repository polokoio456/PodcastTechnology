package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.ui.main.EpisodeAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory { EpisodeAdapter() }
}