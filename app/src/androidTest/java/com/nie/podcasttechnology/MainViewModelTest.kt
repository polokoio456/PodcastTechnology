package com.nie.podcasttechnology

import com.nie.podcasttechnology.data.remote.model.*
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.MainRepository
import com.nie.podcasttechnology.ui.main.MainViewModel
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    private val mainRepository = mockk<MainRepository>(relaxed = true)
    private val databaseRepository = mockk<DatabaseRepository>(relaxed = true)

    private val image = Image(
        "imageUrl",
        "title",
        "link"
    )

    private val enclosure = Enclosure(
        "type",
        "audioUrl"
    )

    private val podcastItem = PodcastItem(
        "title",
        "Sat, 24 Apr 2010 14:01:00 GMT",
        "description",
        "duration",
        enclosure,
        image
    )

    private val channel = Channel(
        "title",
        "Sat, 24 Apr 2010 14:01:00 GMT",
        "description",
        listOf(image),
        listOf(podcastItem)
    )

    private val rss = Rss(channel)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = MainViewModel(mainRepository, databaseRepository)
    }

    @Test
    fun fetchPodcasts() {
        every { databaseRepository.clearAllDatabaseTables() } returns Single.just(true)
        every { mainRepository.fetchPodcasts() } returns Single.just(rss)
        every { databaseRepository.insertPodcasts(rss.channel.items) } returns Completable.complete()

        viewModel.fetchPodcasts()

        verifyOrder {
            databaseRepository.clearAllDatabaseTables()
            mainRepository.fetchPodcasts()
            databaseRepository.insertPodcasts(rss.channel.items)
        }
    }
}