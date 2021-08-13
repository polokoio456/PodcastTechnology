package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { PodcastTechnologyDatabase.getInstance(androidApplication()) }
    single { get<PodcastTechnologyDatabase>().episodeDao() }
}