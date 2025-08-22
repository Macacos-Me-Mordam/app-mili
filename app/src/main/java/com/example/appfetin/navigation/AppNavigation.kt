package com.example.appfetin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appfetin.ui.screens.HomeScreen
import com.example.appfetin.ui.screens.ChatScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chat : Screen("chat")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToChat = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
        
        composable(Screen.Chat.route) {
            ChatScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
