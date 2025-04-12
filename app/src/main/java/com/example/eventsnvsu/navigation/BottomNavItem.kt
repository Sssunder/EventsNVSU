package com.example.eventsnvsu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
