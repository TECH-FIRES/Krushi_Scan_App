package com.example.krushiscan.models

import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("soil_moisture") val soilMoisture: Float,
    @SerializedName("temperature") val temperature: Float,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("ph") val ph: Float
)
