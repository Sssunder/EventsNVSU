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
            },
            onFollow = { isFollowing ->
                coroutineScope.launch {
                    if (isFollowing) {
                        eventViewModel.followEvent(event.id,
                            onSuccess = {
                                android.widget.Toast.makeText(context, "Вы подписались на уведомления о мероприятии", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            onFailure = {
                                android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        eventViewModel.unfollowEvent(event.id,
                            onSuccess = {
                                android.widget.Toast.makeText(context, "Вы отписались от уведомлений о мероприятии", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            onFailure = {
                                android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        )
    }
}
