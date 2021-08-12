package com.nie.podcasttechnology

import android.app.Application
import com.nie.podcasttechnology.module.remoteModule
import com.nie.podcasttechnology.module.repositoryModule
import com.nie.podcasttechnology.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    remoteModule
                )
            )
        }
    }
}