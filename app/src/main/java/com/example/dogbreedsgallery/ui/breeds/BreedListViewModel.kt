package com.example.dogbreedsgallery.ui.breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BreedListUiState(
    val breeds: List<BreedEntity> = emptyList(),
    val query: String = "",
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) {
    val isEmpty: Boolean get() = !isLoading && errorMessage == null && breeds.isEmpty()
}

class BreedListViewModel(
    private val repository: BreedRepository,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val isRefreshing = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(true)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BreedListUiState> = combine(
        repository.observeBreeds(), query, isRefreshing, isLoading, errorMessage,
    ) { breeds, searchQuery, refreshing, loading, error ->
        BreedListUiState(
            breeds = breeds.filter { it.name.contains(searchQuery, ignoreCase = true) },
            query = searchQuery,
            isRefreshing = refreshing,
            isLoading = loading,
            errorMessage = error,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BreedListUiState())

    init {
        refresh()
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            errorMessage.value = null
            try {
                repository.refreshBreeds()
            } catch (exception: Exception) {
                if (repository.getBreedsSnapshot().isEmpty()) {
                    errorMessage.value = exception.localizedMessage ?: "Не удалось загрузить породы"
                }
            } finally {
                isLoading.value = false
                isRefreshing.value = false
            }
        }
    }
}
