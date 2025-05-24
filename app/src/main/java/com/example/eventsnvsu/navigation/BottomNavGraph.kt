package com.example.eventsnvsu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventsnvsu.ui.theme.screens.EventListScreen
import com.example.eventsnvsu.ui.theme.screens.ProfileScreen
import com.example.eventsnvsu.ui.theme.screens.SearchScreen
import com.example.eventsnvsu.ui.theme.screens.EventDetailScreenWrapper
import com.example.eventsnvsu.ui.theme.screens.EditEventScreen
import com.example.eventsnvsu.ui.theme.screens.OrganizerEventsScreen

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
                OrganizerEventsScreen(navController)
            }
            composable(Screen.CreateEvent.route) {
                EditEventScreen(navController, eventId = null)
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
        composable("event_details/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailScreenWrapper(navController, eventId, isOrganizer = isOrganizer)
        }
        composable("edit_event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EditEventScreen(navController, eventId)
        }
    }
}
