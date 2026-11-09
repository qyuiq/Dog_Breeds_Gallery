package com.example.dogbreedsgallery.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.example.dogbreedsgallery.data.local.BreedEntity

@Composable
fun BreedDetailsRoute(viewModel: BreedDetailsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BreedDetailsScreen(state, viewModel::toggleFavorite)
}

@Composable
private fun BreedDetailsScreen(state: BreedDetailsUiState, onFavoriteClick: (BreedEntity) -> Unit) {
    val breed = state.breed
    if (breed == null) {
        androidx.compose.foundation.layout.Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val imageUrls = (state.images.map { it.url } + breed.imageUrl)
        .filter(String::isNotBlank)
        .distinct()
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (imageUrls.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 24.dp),
                pageSpacing = 12.dp,
            ) { page ->
                SubcomposeAsyncImage(
                    model = imageUrls[page],
                    contentDescription = "${breed.name}, фото ${page + 1}",
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentScale = ContentScale.Crop,
                    loading = { com.example.dogbreedsgallery.ui.breeds.ImagePlaceholder("Загружаем фото…", true) },
                    error = { com.example.dogbreedsgallery.ui.breeds.ImagePlaceholder("Фото недоступно") },
                    success = { SubcomposeAsyncImageContent() },
                )
            }
        }
        Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                Text(breed.name, Modifier.weight(1f), style = MaterialTheme.typography.headlineMedium)
                val favoriteScale by animateFloatAsState(
                    targetValue = if (breed.isFavorite) 1.15f else 1f,
                    label = "favoriteScale",
                )
                val favoriteTint by animateColorAsState(
                    targetValue = if (breed.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "favoriteTint",
                )
                IconButton(onClick = { onFavoriteClick(breed) }) {
                    Icon(
                        if (breed.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (breed.isFavorite) "Убрать из избранного" else "В избранное",
                        tint = favoriteTint,
                        modifier = Modifier.scale(favoriteScale),
                    )
                }
            }
            Text("Происхождение: ${breed.origin?.takeIf(String::isNotBlank) ?: "не указано"}")
            Text("Продолжительность жизни: ${breed.lifeSpan.ifBlank { "не указана" }}")
            Text("Темперамент: ${breed.temperament.ifBlank { "не указан" }}")
            Text(
                breed.description?.takeIf(String::isNotBlank)
                    ?.let { "Назначение породы: $it" }
                    ?: "Дополнительная информация о породе отсутствует",
            )
        }
    }
}
