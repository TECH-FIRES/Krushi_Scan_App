package com.example.krushiscan.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recommendation(
    @Json(name = "irrigation_advice") val irrigationAdvice: String,
    @Json(name = "fertilizer_suggestion") val fertilizerSuggestion: String,
    @Json(name = "disease_risk") val diseaseRisk: String,
    @Json(name = "weather_advice") val weatherAdvice: String
)
