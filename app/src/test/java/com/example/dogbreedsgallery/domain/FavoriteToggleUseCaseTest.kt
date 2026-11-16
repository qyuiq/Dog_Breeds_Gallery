package com.example.dogbreedsgallery.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteToggleUseCaseTest {
    private val useCase = FavoriteToggleUseCase()

    @Test
    fun addsBreedToFavorites() {
        assertTrue(useCase.execute(false))
    }

    @Test
    fun removesBreedFromFavorites() {
        assertFalse(useCase.execute(true))
    }
}
