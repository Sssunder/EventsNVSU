package com.example.eventsnvsu.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventsnvsu.ui.theme.screens.CreateEventScreen
import com.example.eventsnvsu.ui.theme.screens.EditEventScreen
import com.example.eventsnvsu.ui.theme.screens.EventDetailScreenWrapper
import com.example.eventsnvsu.ui.theme.screens.EventListScreen
import com.example.eventsnvsu.ui.theme.screens.OrganizerEventsScreen
import com.example.eventsnvsu.ui.theme.screens.ProfileScreen
import com.example.eventsnvsu.ui.theme.screens.SearchScreen
import com.example.eventsnvsu.viewmodel.EventViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController,
    isOrganizer: Boolean,
    authViewModel: com.example.eventsnvsu.viewmodel.AuthViewModel
) {
    val eventViewModel: EventViewModel = viewModel()

    // Добавим тестовое мероприятие для организатора, если его нет
    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (isOrganizer && eventViewModel.events.none { it.title == "Тестовое мероприятие" }) {
            val testEvent = com.example.eventsnvsu.model.Event(
                title = "Тестовое мероприятие",
                description = "Описание тестового мероприятия",
                location = "Онлайн",
                date = "2025-06-03",
                organizerId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )
            eventViewModel.createOrUpdateEvent(testEvent, onSuccess = {}, onFailure = {})
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isOrganizer) Screen.OrganizerEvents.route else Screen.EventList.route
    ) {
        if (isOrganizer) {
            composable(Screen.OrganizerEvents.route) {
                OrganizerEventsScreen(navController)
            }
        } else {
            composable(Screen.EventList.route) {
                EventListScreen(navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    eventViewModel = eventViewModel,
                    onEditEvent = { event ->
                        navController.navigate("edit_event/${event.id}")
                    },
                    onAddEvent = {
                        navController.navigate(Screen.CreateEvent.route)
                    }
                )
            }
        }
        // Делаем маршрут создания мероприятия доступным для всех
        composable(Screen.CreateEvent.route) {
            CreateEventScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, authViewModel)
        }

        composable("event_details/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailScreenWrapper(
                navController,
                eventId,
                isOrganizer = isOrganizer,
                eventViewModel = eventViewModel,
                onEditEvent = {
                    eventId?.let { navController.navigate("edit_event/$it") }
                },
                authViewModel = authViewModel,
                onAddEvent = {
                    navController.navigate(Screen.CreateEvent.route)
                })
        }

        composable("edit_event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EditEventScreen(navController, eventId)
        }

        composable("user_events") {
            com.example.eventsnvsu.ui.theme.screens.UserEventsScreen(navController, eventViewModel)
        }
    }
}
