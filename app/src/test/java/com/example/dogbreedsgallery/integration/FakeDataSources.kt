package com.example.dogbreedsgallery.integration

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.DogApiService
import com.example.dogbreedsgallery.data.remote.model.BreedDto
import com.example.dogbreedsgallery.data.remote.model.BreedImageDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBreedDao(initial: List<BreedEntity> = emptyList()) : BreedDao {
    private val breeds = MutableStateFlow(initial)

    override fun observeBreeds(): Flow<List<BreedEntity>> = breeds
    override suspend fun observeBreedsOnce(): List<BreedEntity> = breeds.value
    override fun observeFavorites(): Flow<List<BreedEntity>> = breeds.map { list -> list.filter { it.isFavorite } }
    override fun observeBreed(breedId: Int): Flow<BreedEntity?> = breeds.map { list -> list.find { it.id == breedId } }

    override suspend fun upsertBreeds(breeds: List<BreedEntity>) {
        val existing = this.breeds.value.associateBy { it.id }
        this.breeds.value = breeds.map { incoming ->
            incoming.copy(isFavorite = existing[incoming.id]?.isFavorite ?: incoming.isFavorite)
        }
    }

    override suspend fun setFavorite(breedId: Int, isFavorite: Boolean) {
        breeds.value = breeds.value.map { breed ->
            if (breed.id == breedId) breed.copy(isFavorite = isFavorite) else breed
        }
    }

    override suspend fun updateImageUrl(breedId: Int, imageUrl: String) {
        breeds.value = breeds.value.map { breed ->
            if (breed.id == breedId) breed.copy(imageUrl = imageUrl) else breed
        }
    }
}

class FakeDogApiService(
    private val remoteBreeds: List<BreedDto> = emptyList(),
    private val images: List<BreedImageDto> = emptyList(),
    private val error: Throwable? = null,
) : DogApiService {
    override suspend fun getBreeds(): List<BreedDto> {
        error?.let { throw it }
        return remoteBreeds
    }

    override suspend fun getBreedImages(breedId: Int, limit: Int): List<BreedImageDto> = images
}
