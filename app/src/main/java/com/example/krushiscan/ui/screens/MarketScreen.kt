package com.example.krushiscan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarketScreen() {
    val marketItems = listOf(
        MarketPrice("Wheat", "₹2,125/quintal", "+2.5%"),
        MarketPrice("Rice (Basmati)", "₹4,500/quintal", "-1.2%"),
        MarketPrice("Cotton", "₹6,800/quintal", "+0.8%"),
        MarketPrice("Sugarcane", "315/quintal", "0.0%"),
        MarketPrice("Maize", "₹1,950/quintal", "+1.5%")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Market Intelligence",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Market is currently Bullish for Grains", fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(marketItems) { item ->
                MarketItemCard(item)
            }
        }
    }
}

data class MarketPrice(val crop: String, val price: String, val trend: String)

@Composable
fun MarketItemCard(item: MarketPrice) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.crop, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text(item.price, fontSize = 14.sp, color = Color.Gray)
            }
            val trendColor = if (item.trend.startsWith("+")) Color(0xFF4CAF50) else if (item.trend.startsWith("-")) Color.Red else Color.Gray
            Text(item.trend, color = trendColor, fontWeight = FontWeight.Bold)
        }
    }
}