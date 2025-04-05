package com.example.eventsnvsu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventsnvsu.ui.EventListScreen
import com.example.eventsnvsu.ui.theme.CreateEventScreen
import com.example.eventsnvsu.ui.theme.LoginScreen
import com.example.eventsnvsu.ui.theme.OrganizerEventListScreen
import com.example.eventsnvsu.ui.theme.ProfileScreen
import com.example.eventsnvsu.ui.theme.RegistrationScreen
import com.example.eventsnvsu.ui.theme.UserEventListScreen
import com.example.eventsnvsu.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    data object Login : Screen("login_screen")
    data object Registration : Screen("registration_screen")
    data object UserEvents : Screen("user_events_screen")
    data object OrganizerEvents : Screen("organizer_events_screen")
    data object CreateEvent : Screen("create_event_screen")
}

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel,modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController, authViewModel) }
        composable(Screen.Registration.route) { RegistrationScreen(navController, authViewModel) }
        composable(Screen.UserEvents.route) { UserEventListScreen(navController) }
        composable(Screen.OrganizerEvents.route) { OrganizerEventListScreen(navController) }
        composable(Screen.CreateEvent.route) { CreateEventScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Chats.route) { ChatsScreen(navController) }
    }
}
