package com.example.krushiscan.data.repository

import com.example.krushiscan.data.api.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class KrushiRepository {
    
    private val apiService = RetrofitClient.apiService
    
    suspend fun getLatestSensorData(): Result<SensorData?> {
        return try {
            val response = apiService.getLatestSensorData()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data)
            } else {
                Result.failure(Exception("Failed to fetch sensor data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getSensorHistory(limit: Int = 20): Result<List<SensorDataItem>> {
        return try {
            val response = apiService.getSensorHistory(limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch sensor history"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun predictDisease(
        imageFile: File,
        state: String? = null,
        district: String? = null
    ): Result<PredictionResponse> {
        return try {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
            
            val stateBody = state?.toRequestBody("text/plain".toMediaTypeOrNull())
            val districtBody = district?.toRequestBody("text/plain".toMediaTypeOrNull())
            
            val response = apiService.predictDisease(imagePart, stateBody, districtBody)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to predict disease"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAlerts(limit: Int = 10): Result<List<Alert>> {
        return try {
            val response = apiService.getAlerts(limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.alerts ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch alerts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getWeather(state: String, district: String): Result<WeatherData?> {
        return try {
            val response = apiService.getWeather(state, district)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.weather)
            } else {
                Result.failure(Exception("Failed to fetch weather"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signup(name: String, email: String, password: String): Result<SignupResponse> {
        return try {
            val response = apiService.signup(SignupRequest(name, email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Signup failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyOtp(email: String, otp: String): Result<VerifyOtpResponse> {
        return try {
            val response = apiService.verifyOtp(VerifyOtpRequest(email, otp))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "OTP verification failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
