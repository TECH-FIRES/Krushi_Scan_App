package com.example.krushiscan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krushiscan.data.repository.KrushiRepository
import com.example.krushiscan.models.CropDisease
import com.example.krushiscan.models.Recommendation
import com.example.krushiscan.models.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class KrushiViewModel : ViewModel() {

    private val repository = KrushiRepository()

    private val _sensorData = MutableStateFlow<SensorData?>(null)
    val sensorData: StateFlow<SensorData?> = _sensorData

    private val _sensorHistory = MutableStateFlow<List<com.example.krushiscan.data.api.SensorDataItem>>(emptyList())
    val sensorHistory: StateFlow<List<com.example.krushiscan.data.api.SensorDataItem>> = _sensorHistory

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
                val result = repository.getLatestSensorData()
                result.onSuccess { apiData ->
                    if (apiData != null) {
                        // Convert data.api.SensorData (Double) -> models.SensorData (Float)
                        _sensorData.value = SensorData(
                            soilMoisture = apiData.soil_moisture?.toFloat() ?: 32f,
                            temperature = apiData.temperature?.toFloat() ?: 28f,
                            humidity = apiData.humidity?.toFloat() ?: 65f,
                            ph = apiData.ph?.toFloat() ?: 6.8f
                        )
                    } else {
                        _sensorData.value = SensorData(32f, 28f, 65f, 6.8f)
                    }
                    _error.value = null
                }.onFailure {
                    // Fallback mock data for demo
                    _sensorData.value = SensorData(32f, 28f, 65f, 6.8f)
                }

                // Also fetch sensor history for chart
                val historyResult = repository.getSensorHistory(limit = 10)
                historyResult.onSuccess { history ->
                    _sensorHistory.value = history
                }
            } catch (e: Exception) {
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
            _error.value = null
            try {
                val response = com.example.krushiscan.data.api.RetrofitClient.apiService.predictDisease(
                    imagePart,
                    null,
                    null
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    val prediction = response.body()!!
                    _cropDisease.value = CropDisease(
                        cropName = prediction.crop ?: prediction.prediction ?: "Unknown",
                        disease = prediction.disease ?: "Unknown",
                        confidence = prediction.confidence?.toFloat() ?: 0f,
                        treatment = prediction.treatment ?: "No treatment data available."
                    )
                    _error.value = null
                } else {
                    // API returned non-success — fall back to mock for demo
                    mockScan()
                }
            } catch (e: Exception) {
                _error.value = "Failed to scan crop: ${e.message}"
                // Fallback to mock for demo
                mockScan()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadCropImageDirect(imagePart: MultipartBody.Part, state: String? = null, district: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = com.example.krushiscan.data.api.RetrofitClient.apiService.predictDisease(
                    imagePart,
                    state?.toRequestBody(),
                    district?.toRequestBody()
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    val prediction = response.body()!!
                    _cropDisease.value = CropDisease(
                        cropName = prediction.crop ?: prediction.prediction ?: "Unknown",
                        disease = prediction.disease ?: "Unknown",
                        confidence = prediction.confidence?.toFloat() ?: 0f,
                        treatment = prediction.treatment ?: "No treatment data available."
                    )
                } else {
                    mockScan()
                }
            } catch (e: Exception) {
                _error.value = "Failed to scan: ${e.message}"
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
                // Recommendations are derived from sensor data + backend logic
                // Provide intelligent mock data based on current sensor readings
                val sensor = _sensorData.value
                _recommendation.value = Recommendation(
                    irrigationAdvice = if (sensor != null && sensor.soilMoisture < 35f)
                        "Soil moisture is low (${sensor.soilMoisture}%). Increase irrigation by 20%. Best time: 5 AM – 8 AM."
                    else "Soil moisture levels are adequate. Monitor daily and irrigate if levels drop below 30%.",
                    fertilizerSuggestion = "Soil pH is ${sensor?.ph ?: 6.8f}. ${
                        if ((sensor?.ph ?: 7f) < 6f) "Apply lime to raise pH before fertilizing."
                        else if ((sensor?.ph ?: 7f) > 7.5f) "Apply sulfur to lower pH. Use NPK 19-19-19 for balanced growth."
                        else "Use NPK 19-19-19 for balanced growth. Apply every 3–4 weeks."
                    }",
                    diseaseRisk = "Humidity at ${sensor?.humidity ?: 65f}%. ${
                        if ((sensor?.humidity ?: 60f) > 70f)
                            "High humidity detected — watch out for Powdery Mildew and Leaf Blight in vine crops."
                        else "Disease risk is moderate. Inspect crops weekly for early signs of infection."
                    }",
                    weatherAdvice = "Current temperature: ${sensor?.temperature ?: 28f}°C. ${
                        if ((sensor?.temperature ?: 28f) > 35f)
                            "High heat stress risk. Consider shade nets and mulching."
                        else "Conditions are favourable for crop growth. No immediate weather action required."
                    }"
                )
                _error.value = null
            } catch (e: Exception) {
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

private fun String.toRequestBody(): okhttp3.RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())