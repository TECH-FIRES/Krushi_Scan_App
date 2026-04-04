package com.example.krushiscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.krushiscan.ui.navigation.Screen
import com.example.krushiscan.ui.screens.*
import com.example.krushiscan.ui.theme.KrushiScanTheme
import com.example.krushiscan.viewmodel.KrushiViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: KrushiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrushiScanTheme {
                MainApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: KrushiViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val mainScreens = listOf(
        Screen.Home.route,
        Screen.Scanner.route,
        Screen.Dashboard.route,
        Screen.Market.route,
        Screen.Region.route
    )

    val showBars = currentDestination?.route in mainScreens

    Scaffold(
        topBar = {
            if (showBars) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "KRUSHI SCAN",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Language selector */ }) {
                            Icon(Icons.Default.Translate, contentDescription = "Language")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Notifications */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                        IconButton(onClick = { /* Profile */ }) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            if (showBars) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    tonalElevation = 8.dp
                ) {
                    val bottomNavItems = listOf(
                        Screen.Home,
                        Screen.Scanner,
                        Screen.Dashboard,
                        Screen.Market,
                        Screen.Region
                    )
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null, modifier = Modifier.size(26.dp)) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(if (showBars) innerPadding else PaddingValues(0.dp))
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignUpScreen(navController) }
            composable("verify_otp/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                VerifyOtpScreen(navController, email)
            }
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Scanner.route) { ScannerScreen(viewModel) }
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
            composable(Screen.Recommendations.route) { RecommendationScreen(viewModel) }
            composable(Screen.Market.route) { MarketScreen() }
            composable(Screen.Region.route) { RegionScreen() }
        }
    }
}
