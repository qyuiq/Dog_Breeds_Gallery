package com.example.dogbreedsgallery.data.remote

import com.example.dogbreedsgallery.data.remote.model.BreedDto
import com.example.dogbreedsgallery.data.remote.model.BreedImageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 20,
    ): List<BreedImageDto>
}
