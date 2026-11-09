package com.example.dogbreedsgallery.data

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.DogApiService
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val breedDao: BreedDao,
    private val dogApi: DogApiService,
) {
    fun observeBreeds(): Flow<List<BreedEntity>> = breedDao.observeBreeds()

    fun observeFavorites(): Flow<List<BreedEntity>> = breedDao.observeFavorites()

    fun observeBreed(id: Int): Flow<BreedEntity?> = breedDao.observeBreed(id)

    suspend fun refreshBreeds() {
        val cachedFavorites = breedDao.observeBreedsOnce().associate { it.id to it.isFavorite }
        val breeds = dogApi.getBreeds().map { breed ->
            BreedEntity(
                id = breed.id,
                name = breed.name,
                temperament = breed.temperament.orEmpty(),
                origin = breed.origin,
                lifeSpan = breed.lifeSpan.orEmpty(),
                description = breed.bredFor,
                imageUrl = breed.referenceImageId?.let { imageId ->
                    "https://cdn2.thedogapi.com/images/$imageId.jpg"
                }.orEmpty(),
                isFavorite = cachedFavorites[breed.id] ?: false,
            )
        }
        breedDao.upsertBreeds(breeds)
    }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) =
        breedDao.setFavorite(id, isFavorite)
}
