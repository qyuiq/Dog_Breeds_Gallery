package com.example.dogbreedsgallery.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.dogbreedsgallery.data.AppDependencies
import com.example.dogbreedsgallery.ui.BreedDetailsViewModelFactory
import com.example.dogbreedsgallery.ui.BreedListViewModelFactory
import com.example.dogbreedsgallery.ui.FavoritesViewModelFactory
import com.example.dogbreedsgallery.ui.breeds.BreedListRoute
import com.example.dogbreedsgallery.ui.breeds.BreedListViewModel
import com.example.dogbreedsgallery.ui.details.BreedDetailsRoute
import com.example.dogbreedsgallery.ui.details.BreedDetailsViewModel
import com.example.dogbreedsgallery.ui.favorites.FavoritesRoute
import com.example.dogbreedsgallery.ui.favorites.FavoritesViewModel

private sealed interface GalleryRoute {
    data object Breeds : GalleryRoute
    data object Favorites : GalleryRoute
    data class Details(val breedId: Int) : GalleryRoute
}

@Composable
fun AppNavigation(dependencies: AppDependencies) {
    val backStack = remember { mutableStateListOf<Any>(GalleryRoute.Breeds) }
    val current = backStack.lastOrNull()
    val showBottomBar = current == GalleryRoute.Breeds || current == GalleryRoute.Favorites

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = current == GalleryRoute.Breeds,
                        onClick = { backStack.replaceRoot(GalleryRoute.Breeds) },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Породы") },
                    )
                    NavigationBarItem(
                        selected = current == GalleryRoute.Favorites,
                        onClick = { backStack.replaceRoot(GalleryRoute.Favorites) },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        label = { Text("Избранное") },
                    )
                }
            }
        },
    ) { padding ->
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
            modifier = Modifier.padding(padding),
            entryProvider = { route ->
                when (route) {
                    GalleryRoute.Breeds -> NavEntry(route) {
                        val vm: BreedListViewModel = viewModel(factory = BreedListViewModelFactory(dependencies))
                        BreedListRoute(vm) { backStack.add(GalleryRoute.Details(it)) }
                    }
                    GalleryRoute.Favorites -> NavEntry(route) {
                        val vm: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory(dependencies))
                        FavoritesRoute(vm) { backStack.add(GalleryRoute.Details(it)) }
                    }
                    is GalleryRoute.Details -> NavEntry(route) {
                        val vm: BreedDetailsViewModel = viewModel(
                            key = "breed-${route.breedId}",
                            factory = BreedDetailsViewModelFactory(route.breedId, dependencies),
                        )
                        BreedDetailsRoute(vm)
                    }
                    else -> error("Неизвестный экран")
                }
            },
        )
    }
}

private fun androidx.compose.runtime.snapshots.SnapshotStateList<Any>.replaceRoot(route: Any) {
    clear()
    add(route)
}
