package com.example.dogbreedsgallery.data.remote.model

import com.squareup.moshi.Json

data class BreedDto(
    val id: Int,
    val name: String,
    val temperament: String?,
    val origin: String?,
    @Json(name = "life_span") val lifeSpan: String?,
    @Json(name = "bred_for") val bredFor: String?,
    @Json(name = "reference_image_id") val referenceImageId: String?,
    val image: BreedImageDto? = null,
)
