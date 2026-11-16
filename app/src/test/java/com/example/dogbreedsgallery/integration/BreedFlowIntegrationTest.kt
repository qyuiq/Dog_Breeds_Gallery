package com.example.dogbreedsgallery.integration

import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.model.BreedDto
import com.example.dogbreedsgallery.ui.breeds.BreedListViewModel
import com.example.dogbreedsgallery.ui.details.BreedDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BreedFlowIntegrationTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun apiRefreshFlowsThroughRepositoryToCatalogState() = runTest {
        val dao = FakeBreedDao()
        val repository = BreedRepository(dao, FakeDogApiService(remoteBreeds = listOf(dto(1, "Akita"))))
        val viewModel = BreedListViewModel(repository)
        val collector = backgroundScope.launch { viewModel.uiState.collect() }

        advanceUntilIdle()

        assertEquals(listOf("Akita"), viewModel.uiState.value.breeds.map { it.name })
        collector.cancel()
    }

    @Test
    fun viewModelSearchQueryUsesDomainFiltering() = runTest {
        val dao = FakeBreedDao()
        val repository = BreedRepository(dao, FakeDogApiService(remoteBreeds = listOf(dto(1, "Akita"), dto(2, "Beagle"))))
        val viewModel = BreedListViewModel(repository)
        val collector = backgroundScope.launch { viewModel.uiState.collect() }

        advanceUntilIdle()
        viewModel.onQueryChange("  akITA ")
        advanceUntilIdle()

        assertEquals(listOf("Akita"), viewModel.uiState.value.breeds.map { it.name })
        collector.cancel()
    }

    @Test
    fun favoriteChangeInDetailsUpdatesLocalData() = runTest {
        val entity = BreedEntity(1, "Akita", "", null, "", null, "")
        val dao = FakeBreedDao(listOf(entity))
        val api = FakeDogApiService()
        val repository = BreedRepository(dao, api)
        val viewModel = BreedDetailsViewModel(1, repository, api)
        val collector = backgroundScope.launch { viewModel.uiState.collect() }

        advanceUntilIdle()
        viewModel.toggleFavorite(entity)
        advanceUntilIdle()

        assertTrue(dao.observeBreedsOnce().single().isFavorite)
        collector.cancel()
    }

    private fun dto(id: Int, name: String) = BreedDto(
        id = id,
        name = name,
        temperament = "",
        origin = null,
        lifeSpan = "10 years",
        bredFor = null,
        referenceImageId = null,
    )
}
