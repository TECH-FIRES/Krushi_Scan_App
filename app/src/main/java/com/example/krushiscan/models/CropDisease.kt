package com.example.krushiscan.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CropDisease(
    @Json(name = "crop_name") val cropName: String,
    @Json(name = "disease") val disease: String,
    @Json(name = "confidence") val confidence: Float,
    @Json(name = "treatment") val treatment: String
)
