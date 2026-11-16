package com.example.dogbreedsgallery.domain

import com.example.dogbreedsgallery.data.local.BreedEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class BreedSearchUseCaseTest {
    private val useCase = BreedSearchUseCase()
    private val breeds = listOf(breed(1, "Akita"), breed(2, "Beagle"))

    @Test
    fun searchIsCaseInsensitiveAndTrimsQuery() {
        val result = useCase.execute(breeds, "  akITA  ")

        assertEquals(listOf("Akita"), result.map { it.name })
    }

    @Test
    fun emptyQueryReturnsAllBreeds() {
        val result = useCase.execute(breeds, "   ")

        assertEquals(breeds, result)
    }

    @Test
    fun searchFindsPartOfBreedName() {
        val result = useCase.execute(breeds, "eag")

        assertEquals(listOf("Beagle"), result.map { it.name })
    }

    @Test
    fun searchWithoutMatchesReturnsEmptyList() {
        val result = useCase.execute(breeds, "husky")

        assertEquals(emptyList<BreedEntity>(), result)
    }

    @Test
    fun searchPreservesOriginalBreedOrder() {
        val result = useCase.execute(listOf(breed(1, "Akita"), breed(2, "Airedale Terrier")), "a")

        assertEquals(listOf("Akita", "Airedale Terrier"), result.map { it.name })
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
