package com.example.dogbreedsgallery.domain

class FavoriteToggleUseCase {
    fun execute(isFavorite: Boolean): Boolean = !isFavorite
}
