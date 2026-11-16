package com.example.dogbreedsgallery.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteToggleUseCaseTest {
    private val useCase = FavoriteToggleUseCase()

    @Test
    fun `добавляет породу в избранное`() {
        assertTrue(useCase.execute(false))
    }

    @Test
    fun `удаляет породу из избранного`() {
        assertFalse(useCase.execute(true))
    }
}
