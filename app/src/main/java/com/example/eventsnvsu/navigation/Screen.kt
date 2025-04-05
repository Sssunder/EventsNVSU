package com.example.eventsnvsu.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login_screen")
    data object Registration : Screen("registration_screen")
    data object EventsList : Screen("events_list_screen") // Лента мероприятий
    data object SearchEvents : Screen("search_events_screen") // Поиск мероприятий
    data object Profile : Screen("profile_screen")
}
