package com.example.krushiscan.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SensorData(
    @Json(name = "soil_moisture") val soilMoisture: Float,
    @Json(name = "temperature") val temperature: Float,
    @Json(name = "humidity") val humidity: Float,
    @Json(name = "ph") val ph: Float
)
