package com.example.eventsnvsu.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eventsnvsu.ui.theme.screens.CreateEventScreen
import com.example.eventsnvsu.ui.theme.screens.EventListScreen
import com.example.eventsnvsu.ui.theme.screens.LoginScreen
import com.example.eventsnvsu.ui.theme.screens.MainScreen
import com.example.eventsnvsu.ui.theme.screens.MainScreen
import com.example.eventsnvsu.ui.theme.screens.OrganizerEventsScreen
import com.example.eventsnvsu.ui.theme.screens.ProfileScreen
import com.example.eventsnvsu.ui.theme.screens.RegistrationScreen
import com.example.eventsnvsu.ui.theme.screens.SearchScreen
import com.example.eventsnvsu.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    isOrganizer: Boolean = false
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController, authViewModel)
        }
        composable(Screen.Main.route) {
            MainScreen(navController, authViewModel, isOrganizer)
        }
    }
}
