package com.example.krushiscan.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.krushiscan.api.ApiClient
import com.example.krushiscan.viewmodel.KrushiViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

@Composable
fun ScannerScreen(viewModel: KrushiViewModel) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }
    var predictionResult by remember { mutableStateOf<String?>(null) }

    val cropDisease by viewModel.cropDisease.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        predictionResult = null
    }

    val apiClient = remember { ApiClient() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "AI Disease Scanner",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Image",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                imageUri?.let { uri ->
                    uploading = true
                    // Convert Uri to real path (you might need your utility function)
                    val path = uri.path ?: ""
                    apiClient.uploadImage(path, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            uploading = false
                            predictionResult = "Upload failed: ${e.message}"
                        }

                        override fun onResponse(call: Call, response: Response) {
                            uploading = false
                            predictionResult = response.body?.string()
                        }
                    })
                }
            },
            enabled = imageUri != null && !uploading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (uploading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Analyze Crop", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        predictionResult?.let {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Prediction Result",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it, fontSize = 16.sp, color = Color.DarkGray)
                }
            }
        }

        cropDisease?.let {
            Spacer(modifier = Modifier.height(24.dp))
            ResultCard(it.cropName, it.disease, it.confidence, it.treatment)
        }
    }
}

@Composable
fun ResultCard(name: String, disease: String, confidence: Float, treatment: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Diagnosis Result",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ResultRow("Crop:", name)
            ResultRow("Disease:", disease)
            ResultRow("Accuracy:", "${(confidence * 100).toInt()}%")
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Treatment Plan:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = treatment,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp),
            color = Color.Gray
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}