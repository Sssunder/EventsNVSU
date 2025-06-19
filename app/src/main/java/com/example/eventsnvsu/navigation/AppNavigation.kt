package com.example.eventsnvsu.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventsnvsu.ui.theme.screens.LoginScreen
import com.example.eventsnvsu.ui.theme.screens.RegistrationScreen
import com.example.eventsnvsu.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    isOrganizer: Boolean = false,
    contentPadding: PaddingValues = PaddingValues()
) {
    val startDestination = if (authViewModel.currentUser != null) Screen.Main.route else Screen.Login.route
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController, authViewModel)
        }
        composable(Screen.Main.route) {
            MainNavigationScaffold(
                navController = navController,
                isOrganizer = isOrganizer,
                authViewModel = authViewModel
            )
        }
        composable(Screen.EmailVerification.route) {
            com.example.eventsnvsu.ui.theme.screens.EmailVerificationScreen(navController, authViewModel)
        }
        composable("user_events") {
            com.example.eventsnvsu.ui.theme.screens.UserEventsScreen(navController)
        }
    }
}
