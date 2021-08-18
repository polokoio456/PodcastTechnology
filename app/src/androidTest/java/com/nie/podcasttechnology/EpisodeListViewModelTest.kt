package com.nie.podcasttechnology

import com.nie.podcasttechnology.data.remote.model.*
import com.nie.podcasttechnology.repository.DatabaseRepository
import com.nie.podcasttechnology.repository.EpisodeListRepository
import com.nie.podcasttechnology.ui.main.EpisodeListViewModel
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class EpisodeListViewModelTest {
    private lateinit var viewModel: EpisodeListViewModel

    private val mainRepository = mockk<EpisodeListRepository>(relaxed = true)
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

    private val podcastItem = EpisodeItem(
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
        viewModel = EpisodeListViewModel(mainRepository, databaseRepository)
    }

    @Test
    fun fetchEpisodes() {
        every { databaseRepository.clearAllDatabaseTables() } returns Single.just(true)
        every { mainRepository.fetchEpisodes() } returns Single.just(rss)
        every { databaseRepository.insertEpisodes(rss.channel.items) } returns Completable.complete()

        viewModel.fetchEpisodes()

        verifyOrder {
            databaseRepository.clearAllDatabaseTables()
            mainRepository.fetchEpisodes()
            databaseRepository.insertEpisodes(rss.channel.items)
        }
    }

    @Test
    fun fetchEpisodesFailed() {
        every { databaseRepository.clearAllDatabaseTables() } returns Single.just(true)
        every { mainRepository.fetchEpisodes() } returns Single.just(rss)
        every { databaseRepository.insertEpisodes(rss.channel.items) } returns Completable.complete()

        viewModel.fetchEpisodes()

        assert(viewModel.serverError.value!!)
    }
}