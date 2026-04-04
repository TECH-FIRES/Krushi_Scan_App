package com.example.krushiscan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krushiscan.viewmodel.KrushiViewModel

data class RecItem(val title: String, val content: String, val icon: ImageVector, val color: Color)

@Composable
fun RecommendationScreen(viewModel: KrushiViewModel) {
    val recommendation by viewModel.recommendation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecommendations()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Smart Recommendations",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            recommendation?.let { data ->
                val items = listOf(
                    RecItem("Irrigation Advice", data.irrigationAdvice, Icons.Default.WaterDrop, Color(0xFF2196F3)),
                    RecItem("Fertilizer Suggestion", data.fertilizerSuggestion, Icons.Default.Agriculture, Color(0xFF4CAF50)),
                    RecItem("Disease Risk", data.diseaseRisk, Icons.Default.Warning, Color(0xFFFF9800)),
                    RecItem("Weather Advice", data.weatherAdvice, Icons.Default.WbSunny, Color(0xFFFFC107))
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items) { item ->
                        RecommendationCard(item)
                    }
                }
            } ?: Text("No recommendations available.")
        }
    }
}

@Composable
fun RecommendationCard(item: RecItem) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.Top) {
            Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.content, fontSize = 14.sp, color = Color.DarkGray)
            }
        }
    }
}