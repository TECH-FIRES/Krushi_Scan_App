package com.example.krushiscan.models

import com.google.gson.annotations.SerializedName

data class Recommendation(
    @SerializedName("irrigation_advice") val irrigationAdvice: String,
    @SerializedName("fertilizer_suggestion") val fertilizerSuggestion: String,
    @SerializedName("disease_risk") val diseaseRisk: String,
    @SerializedName("weather_advice") val weatherAdvice: String
)
