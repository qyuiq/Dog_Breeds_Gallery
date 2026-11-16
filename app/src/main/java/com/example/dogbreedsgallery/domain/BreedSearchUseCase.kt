package com.example.dogbreedsgallery.domain

import com.example.dogbreedsgallery.data.local.BreedEntity

class BreedSearchUseCase {
    fun execute(breeds: List<BreedEntity>, query: String): List<BreedEntity> = emptyList()
}
