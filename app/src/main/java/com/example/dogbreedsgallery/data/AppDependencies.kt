package com.example.dogbreedsgallery.data

import android.content.Context
import androidx.room.Room
import com.example.dogbreedsgallery.data.local.DogBreedsDatabase
import com.example.dogbreedsgallery.data.remote.DogApiFactory

class AppDependencies private constructor(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        DogBreedsDatabase::class.java,
        "dog_breeds.db",
    ).build()

    val breedRepository = BreedRepository(
        breedDao = database.breedDao(),
        dogApi = DogApiFactory.create(),
    )

    companion object {
        @Volatile
        private var instance: AppDependencies? = null

        fun get(context: Context): AppDependencies =
            instance ?: synchronized(this) {
                instance ?: AppDependencies(context).also { instance = it }
            }
    }
}
