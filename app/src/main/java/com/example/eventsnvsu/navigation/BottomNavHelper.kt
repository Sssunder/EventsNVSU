package com.example.eventsnvsu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun getIconForRoute(route: String): ImageVector = when (route) {
    Screen.UserEvents.route, Screen.OrganizerEvents.route -> Icons.Default.Event
    Screen.CreateEvent.route -> Icons.Default.Add
    Screen.Chats.route -> Icons.Default.Chat
    Screen.Profile.route -> Icons.Default.Person
    else -> Icons.Default.Help
}

fun getLabelForRoute(route: String): String = when (route) {
    Screen.UserEvents.route -> "Мероприятия"
    Screen.OrganizerEvents.route -> "Мои события"
    Screen.CreateEvent.route -> "Создать"
    Screen.Chats.route -> "Чаты"
    Screen.Profile.route -> "Профиль"
    else -> "Экран"
}
