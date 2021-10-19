package com.nie.podcasttechnology.module

import com.nie.podcasttechnology.data.database.PodcastTechnologyDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { PodcastTechnologyDatabase.provideDatabase(androidContext()) }
    single { get<PodcastTechnologyDatabase>().episodeDao() }
}