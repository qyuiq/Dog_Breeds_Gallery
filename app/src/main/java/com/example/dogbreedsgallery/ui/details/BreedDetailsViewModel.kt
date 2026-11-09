package com.example.dogbreedsgallery.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.DogApiService
import com.example.dogbreedsgallery.data.remote.model.BreedImageDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BreedDetailsUiState(
    val breed: BreedEntity? = null,
    val images: List<BreedImageDto> = emptyList(),
    val isLoadingImages: Boolean = false,
)

class BreedDetailsViewModel(
    private val breedId: Int,
    private val repository: BreedRepository,
    private val dogApi: DogApiService,
) : ViewModel() {
    private val images = MutableStateFlow<List<BreedImageDto>>(emptyList())
    private val isLoadingImages = MutableStateFlow(false)

    val uiState: StateFlow<BreedDetailsUiState> = kotlinx.coroutines.flow.combine(
        repository.observeBreed(breedId), images, isLoadingImages,
    ) { breed, gallery, loading -> BreedDetailsUiState(breed, gallery, loading) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BreedDetailsUiState())

    init {
        loadImages()
    }

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch { repository.setFavorite(breed.id, !breed.isFavorite) }
    }

    private fun loadImages() {
        viewModelScope.launch {
            isLoadingImages.value = true
            try {
                images.value = dogApi.getBreedImages(breedId)
                images.value.firstOrNull()?.url?.takeIf(String::isNotBlank)?.let { imageUrl ->
                    repository.updateBreedImage(breedId, imageUrl)
                }
            } catch (_: Exception) {
                // The cached breed image remains available when the gallery request fails.
            } finally {
                isLoadingImages.value = false
            }
        }
    }
}
