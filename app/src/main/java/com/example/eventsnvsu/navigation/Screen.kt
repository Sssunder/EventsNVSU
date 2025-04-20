package com.example.eventsnvsu.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Main : Screen("main")
    object EventList : Screen("event_list")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object OrganizerEvents : Screen("organizer_events")
    object CreateEvent : Screen("create_event")
    object EventDetails : Screen("event_details/{eventId}") {
        fun createRoute(eventId: String) = "event_details/$eventId"
    }
}