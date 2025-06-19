package com.example.eventsnvsu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

val bottomNavItems = listOf(
    BottomNavItem("Главная", Icons.Default.Home, Screen.Main),
    BottomNavItem("Поиск", Icons.Default.Search, Screen.Search),
    BottomNavItem("Профиль", Icons.Default.Person, Screen.Profile)
)

fun getBottomNavItems(isOrganizer: Boolean): List<BottomNavItem> = if (isOrganizer) listOf(
    BottomNavItem("Мои события", Icons.Default.DateRange, Screen.OrganizerEvents),
    BottomNavItem("Создать", Icons.Default.Add, Screen.CreateEvent),
    BottomNavItem("Профиль", Icons.Default.Person, Screen.Profile)
) else listOf(
    BottomNavItem("Мероприятия", Icons.Default.DateRange, Screen.EventList),
    BottomNavItem("Поиск", Icons.Default.Search, Screen.Search),
    BottomNavItem("Профиль", Icons.Default.Person, Screen.Profile)
)
