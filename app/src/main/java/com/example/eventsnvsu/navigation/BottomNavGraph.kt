package com.example.eventsnvsu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventsnvsu.ui.theme.screens.EventListScreen
import com.example.eventsnvsu.ui.theme.screens.ProfileScreen
import com.example.eventsnvsu.ui.theme.screens.SearchScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    isOrganizer: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isOrganizer) Screen.OrganizerEvents.route else Screen.EventList.route
    ) {
        if (isOrganizer) {
            composable(Screen.OrganizerEvents.route) {
                // TODO: OrganizerEventsScreen(navController)
            }
            composable(Screen.CreateEvent.route) {
                // TODO: CreateEventScreen(navController)
            }
        } else {
            composable(Screen.EventList.route) {
                EventListScreen(navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController, isOrganizer)
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, isOrganizer)
        }
    }
}
