package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.bean.AudioPlayer
import org.koin.dsl.module

val beanModule = module {
    single { AudioPlayer() }
}