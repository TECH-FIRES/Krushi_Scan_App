package com.example.krushiscan.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @GET("api/sensor-data")
    suspend fun getLatestSensorData(): Response<SensorDataResponse>
    
    @GET("api/hardware/ingest")
    suspend fun getSensorHistory(
        @Query("limit") limit: Int = 20,
        @Query("device_id") deviceId: String? = null
    ): Response<SensorHistoryResponse>
    
    @Multipart
    @POST("api/ai-predict")
    suspend fun predictDisease(
        @Part image: MultipartBody.Part,
        @Part("state") state: RequestBody? = null,
        @Part("district") district: RequestBody? = null
    ): Response<PredictionResponse>
    
    @GET("api/alerts")
    suspend fun getAlerts(
        @Query("limit") limit: Int = 10
    ): Response<AlertsResponse>
    
    @GET("api/weather")
    suspend fun getWeather(
        @Query("state") state: String,
        @Query("district") district: String
    ): Response<WeatherResponse>
    
    @POST("api/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>
    
    @POST("api/auth/signup")
    suspend fun signup(@Body userData: SignupRequest): Response<SignupResponse>
    
    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body otpData: VerifyOtpRequest): Response<VerifyOtpResponse>
}

data class SensorDataResponse(
    val success: Boolean,
    val data: SensorData?
)

data class SensorData(
    val soil_moisture: Double?,
    val temperature: Double?,
    val humidity: Double?,
    val water_temperature: Double?,
    val ph: Double?,
    val timestamp: String?
)

data class SensorHistoryResponse(
    val success: Boolean,
    val count: Int,
    val data: List<SensorDataItem>
)

data class SensorDataItem(
    val _id: String,
    val device_id: String,
    val soil_moisture: Double,
    val temperature: Double,
    val humidity: Double,
    val water_temperature: Double,
    val ph: Double,
    val timestamp: String
)

data class PredictionResponse(
    val success: Boolean,
    val prediction: String?,
    val crop: String?,
    val disease: String?,
    val confidence: Double?,
    val treatment: String?,
    val recommended_crops: List<RecommendedCrop>?,
    val top5_predictions: List<TopPrediction>?
)

data class RecommendedCrop(
    val crop: String,
    val score: Double
)

data class TopPrediction(
    val `class`: String,
    val confidence: Double
)

data class AlertsResponse(
    val success: Boolean,
    val alerts: List<Alert>
)

data class Alert(
    val _id: String,
    val type: String,
    val message: String,
    val sensorKey: String,
    val sensorValue: Double,
    val threshold: Double,
    val createdAt: String,
    val read: Boolean
)

data class WeatherResponse(
    val success: Boolean,
    val weather: WeatherData?
)

data class WeatherData(
    val temperature: Double,
    val humidity: Double,
    val description: String,
    val icon: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class LoginResponse(
    val message: String,
    val user: User
)

data class SignupResponse(
    val message: String
)

data class VerifyOtpResponse(
    val message: String
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val farmName: String?,
    val farmSize: String?,
    val state: String?,
    val district: String?,
    val village: String?,
    val cropsGrown: List<String>?,
    val bio: String?,
    val soilHealth: String?,
    val totalFields: Int?,
    val totalSensors: Int?,
    val memberSince: String?,
    val profileSettings: ProfileSettings?
)

data class ProfileSettings(
    val weatherAlerts: Boolean,
    val marketAlerts: Boolean,
    val emailNotifications: Boolean,
    val smsNotifications: Boolean,
    val language: String
)
