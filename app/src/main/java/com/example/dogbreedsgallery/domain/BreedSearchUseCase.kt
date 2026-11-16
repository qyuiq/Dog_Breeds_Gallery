package com.example.dogbreedsgallery.domain

import com.example.dogbreedsgallery.data.local.BreedEntity

class BreedSearchUseCase {
    fun execute(breeds: List<BreedEntity>, query: String): List<BreedEntity> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isEmpty()) return breeds

        return breeds.filter { breed ->
            breed.name.contains(normalizedQuery, ignoreCase = true)
        }
    }
}
