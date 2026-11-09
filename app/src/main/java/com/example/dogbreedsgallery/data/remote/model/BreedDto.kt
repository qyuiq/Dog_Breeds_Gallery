package com.example.dogbreedsgallery.data.remote.model

import com.squareup.moshi.Json

data class BreedDto(
    val id: Int,
    val name: String,
    val temperament: String?,
    val origin: String?,
    @Json(name = "life_span") val lifeSpan: String?,
    val bredFor: String?,
    val referenceImageId: String?,
)
