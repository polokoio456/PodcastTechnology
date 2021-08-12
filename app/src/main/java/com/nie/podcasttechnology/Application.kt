package com.nie.podcasttechnology

import android.app.Application
import com.nie.podcasttechnology.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(
                listOf(
                    adapterModule,
                    viewModelModule,
                    repositoryModule,
                    remoteModule,
                    utilModule
                )
            )
        }
    }
}