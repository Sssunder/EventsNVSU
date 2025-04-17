package com.example.eventsnvsu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.eventsnvsu.R

@Composable
fun getIconForRoute(route: String): ImageVector = when (route) {
    Screen.EventList.route, Screen.OrganizerEvents.route -> ImageVector.vectorResource(id = R.drawable.eventicon)
    Screen.CreateEvent.route -> Icons.Default.Add
    Screen.Search.route -> Icons.Default.Search
    Screen.Profile.route -> Icons.Default.Person
    else -> Icons.Default.Info
}

fun getLabelForRoute(route: String): String = when (route) {
    Screen.EventList.route -> "Мероприятия"
    Screen.OrganizerEvents.route -> "Мои события"
    Screen.CreateEvent.route -> "Создать"
    Screen.Search.route -> "Поиск"
    Screen.Profile.route -> "Профиль"
    else -> "Экран"
}
