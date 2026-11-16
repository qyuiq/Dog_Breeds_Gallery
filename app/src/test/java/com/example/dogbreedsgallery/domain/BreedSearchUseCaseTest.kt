package com.example.dogbreedsgallery.domain

import com.example.dogbreedsgallery.data.local.BreedEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class BreedSearchUseCaseTest {
    private val useCase = BreedSearchUseCase()
    private val breeds = listOf(breed(1, "Akita"), breed(2, "Beagle"))

    @Test
    fun `поиск игнорирует регистр и пробелы по краям запроса`() {
        val result = useCase.execute(breeds, "  akITA  ")

        assertEquals(listOf("Akita"), result.map { it.name })
    }

    private fun breed(id: Int, name: String) = BreedEntity(
        id = id,
        name = name,
        temperament = "",
        origin = null,
        lifeSpan = "",
        description = null,
        imageUrl = "",
    )
}
