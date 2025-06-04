package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eventsnvsu.ui.theme.EventCard
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.items

@Composable
fun OrganizerEventsScreen(navController: NavController, eventViewModel: EventViewModel = viewModel()) {
    val events = eventViewModel.events
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    var lastClickTime by remember { mutableStateOf(0L) }
    val doubleClickThreshold = 300L

    LaunchedEffect(Unit) {
        eventViewModel.loadOrganizerEvents(currentUserId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Мои мероприятия",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(events, key = { it.id }) { event ->
                EventCard(event = event) {
                    val now = System.currentTimeMillis()
                    if (now - lastClickTime < doubleClickThreshold) {
                        navController.navigate("edit_event/${event.id}")
                    } else {
                        navController.navigate("event_details/${event.id}")
                    }
                    lastClickTime = now
                }
            }
        }
    }
}

