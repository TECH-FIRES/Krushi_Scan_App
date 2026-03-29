package com.example.krushiscan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.krushiscan.viewmodel.KrushiViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

data class StatItem(val label: String, val value: String, val icon: ImageVector, val color: Color)

@Composable
fun DashboardScreen(viewModel: KrushiViewModel) {
    val sensorData by viewModel.sensorData.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.fetchSensorData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Field Dashboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Default values if API fails/is empty for demo
        val displayData = sensorData ?: com.example.krushiscan.models.SensorData(30f, 35f, 70f, 6.5f)
        
        val stats = listOf(
            StatItem("Moisture", "${displayData.soilMoisture}%", Icons.Default.WaterDrop, Color(0xFF2196F3)),
            StatItem("Temp", "${displayData.temperature}°C", Icons.Default.DeviceThermostat, Color(0xFFFF5722)),
            StatItem("Humidity", "${displayData.humidity}%", Icons.Default.Cloud, Color(0xFF00BCD4)),
            StatItem("pH Level", "${displayData.ph}", Icons.Default.Science, Color(0xFF9C27B0))
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().height(240.dp)
        ) {
            items(stats) { item ->
                StatCard(item)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Moisture Trend",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                SensorChart()
            }
        }
    }
}

@Composable
fun StatCard(item: StatItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(item.label, fontSize = 12.sp, color = Color.Gray)
                Text(item.value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SensorChart() {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val entries = ArrayList<Entry>()
                entries.add(Entry(0f, 30f))
                entries.add(Entry(1f, 32f))
                entries.add(Entry(2f, 28f))
                entries.add(Entry(3f, 35f))
                entries.add(Entry(4f, 33f))
                entries.add(Entry(5f, 31f))

                val dataSet = LineDataSet(entries, "Soil Moisture (%)").apply {
                    color = android.graphics.Color.parseColor("#4CAF50")
                    valueTextColor = android.graphics.Color.BLACK
                    lineWidth = 2f
                    setCircleColor(android.graphics.Color.parseColor("#4CAF50"))
                    setDrawFilled(true)
                    fillColor = android.graphics.Color.parseColor("#C8E6C9")
                }
                data = LineData(dataSet)
                description.isEnabled = false
                legend.isEnabled = true
                animateX(1000)
                invalidate()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
