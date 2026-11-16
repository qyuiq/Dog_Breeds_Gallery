package com.example.dogbreedsgallery.ui.favorites

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun пустоеИзбранноеПоказываетПонятноеСообщение() {
        composeRule.setContent {
            DogBreedsGalleryTheme {
                FavoritesScreen(emptyList()) {}
            }
        }

        composeRule.onNodeWithText("В избранном пока пусто").assertIsDisplayed()
    }

    @Test
    fun избранноеПоказываетСохраненнуюПороду() {
        composeRule.setContent {
            DogBreedsGalleryTheme {
                FavoritesScreen(listOf(breed("Akita"))) {}
            }
        }

        composeRule.onNodeWithText("Akita").assertIsDisplayed()
    }

    private fun breed(name: String) = BreedEntity(
        id = 1,
        name = name,
        temperament = "",
        origin = null,
        lifeSpan = "",
        description = null,
        imageUrl = "",
        isFavorite = true,
    )
}
