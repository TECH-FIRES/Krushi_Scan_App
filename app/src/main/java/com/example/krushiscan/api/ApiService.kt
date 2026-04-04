package com.example.krushiscan.api

import com.example.krushiscan.models.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("api/ai-predict")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): CropDisease

    @GET("sensor-data")
    suspend fun getSensorData(): SensorData

    @GET("recommendation")
    suspend fun getRecommendations(): Recommendation

    @GET("region-data")
    suspend fun getRegionData(
        @Query("state") state: String,
        @Query("district") district: String
    ): RegionData

    @GET("weather")
    suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): WeatherData
}

data class RegionData(
    val crops: List<String>,
    val diseases: List<String>,
    val soilType: String
)

data class WeatherData(
    val temperature: Float,
    val description: String,
    val icon: String
)
