package com.example.krushiscan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krushiscan.api.RetrofitInstance
import com.example.krushiscan.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class KrushiViewModel : ViewModel() {

    private val _sensorData = MutableStateFlow<SensorData?>(null)
    val sensorData: StateFlow<SensorData?> = _sensorData

    private val _cropDisease = MutableStateFlow<CropDisease?>(null)
    val cropDisease: StateFlow<CropDisease?> = _cropDisease

    private val _recommendation = MutableStateFlow<Recommendation?>(null)
    val recommendation: StateFlow<Recommendation?> = _recommendation

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchSensorData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // For demo, if API fails, we provide mock data after a short delay
                _sensorData.value = RetrofitInstance.api.getSensorData()
                _error.value = null
            } catch (e: Exception) {
                // Mock data for hackathon demo
                _sensorData.value = SensorData(32f, 28f, 65f, 6.8f)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun mockScan() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000) // Simulate processing
            _cropDisease.value = CropDisease(
                cropName = "Tomato",
                disease = "Early Blight",
                confidence = 0.94f,
                treatment = "Apply fungicide containing chlorothalonil or copper. Remove infected lower leaves to improve air circulation."
            )
            _isLoading.value = false
        }
    }

    fun uploadCropImage(imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _cropDisease.value = RetrofitInstance.api.uploadImage(imagePart)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to scan crop: ${e.message}"
                // Fallback to mock for demo
                mockScan()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _recommendation.value = RetrofitInstance.api.getRecommendations()
                _error.value = null
            } catch (e: Exception) {
                // Mock data for demo
                _recommendation.value = Recommendation(
                    irrigationAdvice = "Increase irrigation by 20% due to rising temperatures. Best time: 5 AM - 8 AM.",
                    fertilizerSuggestion = "Soil nitrogen is slightly low. Use NPK 19-19-19 for balanced growth.",
                    diseaseRisk = "High humidity detected. Watch out for Powdery Mildew in vine crops.",
                    weatherAdvice = "Heavy rain expected in 48 hours. Postpone pesticide application."
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
