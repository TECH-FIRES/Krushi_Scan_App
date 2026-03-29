package com.example.krushiscan.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Scanner : Screen("scanner", "Scanner", Icons.Default.Search)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Info)
    object Recommendations : Screen("recommendations", "Tips", Icons.Default.List)
    object Market : Screen("market", "Market", Icons.Default.ShoppingCart)
    object Region : Screen("region", "Region", Icons.Default.LocationOn)
}
