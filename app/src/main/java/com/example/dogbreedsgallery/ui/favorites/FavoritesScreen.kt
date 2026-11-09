package com.example.dogbreedsgallery.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.dogbreedsgallery.data.local.BreedEntity

@Composable
fun FavoritesRoute(viewModel: FavoritesViewModel, onBreedClick: (Int) -> Unit) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    FavoritesScreen(favorites, onBreedClick)
}

@Composable
private fun FavoritesScreen(favorites: List<BreedEntity>, onBreedClick: (Int) -> Unit) {
    if (favorites.isEmpty()) {
        androidx.compose.foundation.layout.Box(
            Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) { Text("В избранном пока пусто") }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(favorites, key = { it.id }) { breed ->
                Column(Modifier.fillMaxWidth().clickable { onBreedClick(breed.id) }) {
                    AsyncImage(
                        model = breed.imageUrl,
                        contentDescription = "Порода ${breed.name}",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(breed.name, Modifier.padding(top = 8.dp), style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}
