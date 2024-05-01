package com.app.geofence

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.geofence.screens.HomeScreen
import com.app.geofence.screens.MapScreen


internal sealed class Screen(val route: String) {
    data object HomeScreen : Screen("Home")
    data object MapScreen : Screen("map_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.MapScreen.route) {
            MapScreen(navController)
        }
    }
}