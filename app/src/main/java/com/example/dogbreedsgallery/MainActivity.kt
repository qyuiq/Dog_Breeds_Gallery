package com.example.dogbreedsgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dogbreedsgallery.data.AppDependencies
import com.example.dogbreedsgallery.ui.navigation.AppNavigation
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogBreedsGalleryTheme {
                AppNavigation(AppDependencies.get(this))
            }
        }
    }
}

