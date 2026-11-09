package com.example.dogbreedsgallery.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds ORDER BY name COLLATE NOCASE")
    fun observeBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name COLLATE NOCASE")
    fun observeFavorites(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :breedId LIMIT 1")
    fun observeBreed(breedId: Int): Flow<BreedEntity?>

    @Upsert
    suspend fun upsertBreeds(breeds: List<BreedEntity>)

    @Query("UPDATE breeds SET isFavorite = :isFavorite WHERE id = :breedId")
    suspend fun setFavorite(breedId: Int, isFavorite: Boolean)
}
