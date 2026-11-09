package com.example.dogbreedsgallery.data.remote.model

import com.squareup.moshi.Json

data class BreedImageDto(
    val id: String,
    val url: String,
    @Json(name = "width") val width: Int?,
    @Json(name = "height") val height: Int?,
)
