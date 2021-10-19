package com.nie.podcasttechnology

import android.app.Application
import com.nie.podcasttechnology.module.*
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@FlowPreview
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                    adapterModule,
                    beanModule,
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule
                )
            )
        }
    }
}