package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.example.eventsnvsu.ui.theme.EventCard
import androidx.navigation.NavController

@Composable
fun UserEventsScreen(navController: NavController, eventViewModel: EventViewModel = viewModel()) {
    val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val events = eventViewModel.events.filter { it.participants?.contains(currentUserId) == true }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        eventViewModel.observeAllEvents()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Мероприятия пользователя", style = MaterialTheme.typography.headlineSmall)
        androidx.compose.foundation.lazy.LazyColumn {
            items(events) { event ->
                EventCard(event = event) {
                    navController.navigate("event_details/${event.id}")
                }
            }
        }
    }
}
