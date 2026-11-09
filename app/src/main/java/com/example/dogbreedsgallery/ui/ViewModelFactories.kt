package com.example.dogbreedsgallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogbreedsgallery.data.AppDependencies
import com.example.dogbreedsgallery.data.remote.DogApiFactory
import com.example.dogbreedsgallery.ui.breeds.BreedListViewModel
import com.example.dogbreedsgallery.ui.details.BreedDetailsViewModel
import com.example.dogbreedsgallery.ui.favorites.FavoritesViewModel

class BreedListViewModelFactory(
    dependencies: AppDependencies,
) : ViewModelProvider.Factory {
    private val repository = dependencies.breedRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(BreedListViewModel::class.java))
        return BreedListViewModel(repository) as T
    }
}

class FavoritesViewModelFactory(
    dependencies: AppDependencies,
) : ViewModelProvider.Factory {
    private val repository = dependencies.breedRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(FavoritesViewModel::class.java))
        return FavoritesViewModel(repository) as T
    }
}

class BreedDetailsViewModelFactory(
    private val breedId: Int,
    dependencies: AppDependencies,
) : ViewModelProvider.Factory {
    private val repository = dependencies.breedRepository
    private val dogApi = DogApiFactory.create()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(BreedDetailsViewModel::class.java))
        return BreedDetailsViewModel(breedId, repository, dogApi) as T
    }
}
