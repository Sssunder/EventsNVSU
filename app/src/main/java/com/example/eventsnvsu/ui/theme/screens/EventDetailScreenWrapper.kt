package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.eventsnvsu.viewmodel.EventViewModel
import kotlinx.coroutines.launch

@Composable
fun EventDetailScreenWrapper(
    navController: NavHostController,
    eventId: String?,
    isOrganizer: Boolean,
    authViewModel: com.example.eventsnvsu.viewmodel.AuthViewModel,
    eventViewModel: EventViewModel,
    onEditEvent: () -> Unit,
    onAddEvent: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val event = eventViewModel.events.find { it.id == eventId }
    val isAdmin = authViewModel.currentUserRole.value == "admin"

    LaunchedEffect(eventId) {
        eventViewModel.observeAllEvents()
    }

    if (event != null) {
        EventDetailScreen(
            navController = navController,
            event = event,
            isOrganizer = isOrganizer,
            isAdmin = isAdmin,
            onEdit = {
                onEditEvent()
            },
            onRegister = {
                coroutineScope.launch {
                    eventViewModel.registerForEvent(event.id)
                }
            }
        )
    }
}
