package com.example.krushiscan.models

import com.google.gson.annotations.SerializedName

data class CropDisease(
    @SerializedName("prediction") val cropName: String,
    @SerializedName("disease") val disease: String,
    @SerializedName("confidence") val confidence: Float,
    @SerializedName("treatment") val treatment: String
)
