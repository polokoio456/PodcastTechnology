package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.bean.AudioPlayer
import org.koin.dsl.module

val utilModule = module {
    single { AudioPlayer() }
}