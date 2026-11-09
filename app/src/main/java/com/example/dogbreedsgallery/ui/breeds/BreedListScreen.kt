package com.example.dogbreedsgallery.ui.breeds

import androidx.compose.foundation.clickable
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.dogbreedsgallery.data.local.BreedEntity

@Composable
fun BreedListRoute(
    viewModel: BreedListViewModel,
    listState: LazyListState,
    onBreedClick: (Int) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { message -> snackbarHostState.showSnackbar(message) }
    }
    BreedListScreen(state, viewModel::onQueryChange, viewModel::refresh, onBreedClick, snackbarHostState, listState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreedListScreen(
    state: BreedListUiState,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onBreedClick: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    listState: LazyListState,
) {
    LaunchedEffect(state.query) {
        listState.scrollToItem(0)
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Найти породу") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
            )
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                when {
                    state.isLoading && state.breeds.isEmpty() ->
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    state.errorMessage != null && state.breeds.isEmpty() ->
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
                        }
                    state.breeds.isEmpty() ->
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Породы не найдены")
                        }
                    else -> LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.breeds, key = { it.id }) { breed ->
                            BreedCard(
                                breed,
                                modifier = Modifier.animateContentSize(),
                                onClick = { onBreedClick(breed.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BreedCard(breed: BreedEntity, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SubcomposeAsyncImage(
            model = breed.imageUrl,
            contentDescription = "Порода ${breed.name}",
            modifier = Modifier.fillMaxWidth().height(190.dp),
            contentScale = ContentScale.Crop,
            loading = { ImagePlaceholder("Загружаем фото…", loading = true) },
            error = { ImagePlaceholder("Фото недоступно") },
            success = { SubcomposeAsyncImageContent() },
        )
        Text(breed.name, style = MaterialTheme.typography.titleLarge)
        Text(
            breed.description?.takeIf(String::isNotBlank) ?: breed.temperament.ifBlank { "Описание пока недоступно" },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
        )
    }
}

@Composable
internal fun ImagePlaceholder(message: String, loading: Boolean = false) {
    Box(
        Modifier.fillMaxSize().padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) CircularProgressIndicator()
        else Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
