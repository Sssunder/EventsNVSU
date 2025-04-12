package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eventsnvsu.navigation.AppNavigation
import com.example.eventsnvsu.navigation.Screen
import com.example.eventsnvsu.navigation.getIconForRoute
import com.example.eventsnvsu.navigation.getLabelForRoute
import com.example.eventsnvsu.viewmodel.AuthViewModel


@Composable
fun MainScreen(navController: NavHostController, authViewModel: AuthViewModel, isOrganizer: Boolean) {
    val screens = if (isOrganizer) {
        listOf(
            Screen.OrganizerEvents,
            Screen.CreateEvent,
            Screen.Profile
        )
    } else {
        listOf(
            Screen.UserEvents,
            Screen.Chats,
            Screen.Profile
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(getIconForRoute(screen.route), contentDescription = null) },
                        label = { Text(getLabelForRoute(screen.route)) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            authViewModel = authViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

