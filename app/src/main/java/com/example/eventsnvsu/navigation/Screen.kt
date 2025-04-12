package com.example.eventsnvsu.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login_screen")
    data object Registration : Screen("registration_screen")
    data object Main : Screen("main_screen")
    data object Search : Screen("search_screen")
    data object Profile : Screen("profile_screen")
}
