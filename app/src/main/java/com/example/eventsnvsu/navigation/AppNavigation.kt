package com.example.eventsnvsu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventsnvsu.MainScreen
import com.example.eventsnvsu.ui.theme.screens.LoginScreen
import com.example.eventsnvsu.ui.theme.screens.ProfileScreen
import com.example.eventsnvsu.ui.theme.screens.RegistrationScreen
import com.example.eventsnvsu.ui.theme.screens.SearchScreen
import com.example.eventsnvsu.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    isOrganizer: Boolean = false // передаёшь при запуске из MainActivity, например
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController, authViewModel)
        }
        composable(Screen.Main.route) {
            MainScreen(navController, isOrganizer)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController, isOrganizer)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController, isOrganizer)
        }
    }
}

