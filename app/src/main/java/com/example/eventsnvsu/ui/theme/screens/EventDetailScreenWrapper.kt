package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.ui.theme.screens.EventDetailScreen
import kotlinx.coroutines.launch

@Composable
fun EventDetailScreenWrapper(
    navController: NavController,
    eventId: String?,
    eventViewModel: EventViewModel = viewModel(),
    isOrganizer: Boolean = false
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val event = eventViewModel.events.find { it.id == eventId }

    LaunchedEffect(eventId) {
        eventViewModel.observeAllEvents()
    }

    if (event != null) {
        EventDetailScreen(
            navController = navController,
            event = event,
            isOrganizer = isOrganizer,
            onEdit = {
                navController.navigate("edit_event/${event.id}")
            },
            onRegister = {
                coroutineScope.launch {
                    eventViewModel.registerForEvent(event.id ?: "")
                }
            }
        )
    }
}

