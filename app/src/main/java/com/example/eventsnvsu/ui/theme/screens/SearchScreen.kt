package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.ui.theme.EventCard
import com.example.eventsnvsu.viewmodel.AuthViewModel
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SearchScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    eventViewModel: EventViewModel,
    onEditEvent: (Event) -> Unit,
    onAddEvent: () -> Unit
) {
    val events = eventViewModel.events
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val isOrganizer = authViewModel.currentUserRole.value == "organizer"
    val isAdmin = authViewModel.currentUserRole.value == "admin"
    val canEdit = isOrganizer || isAdmin
    // Добавляем вызов observeAllEvents для загрузки мероприятий
    LaunchedEffect(Unit) {
        eventViewModel.observeAllEvents()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(androidx.compose.foundation.rememberScrollState())
    ) {
        Text(text = "Поиск мероприятий", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        // Фильтры поиска (по тегам и названию)
        var filterText by rememberSaveable { mutableStateOf("") }
        var selectedTag by rememberSaveable { mutableStateOf("") }
        val allTags = events.flatMap { it.tags }.distinct().filter { it.isNotBlank() }
        TextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text("Поиск по названию") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (allTags.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allTags) { tag ->
                    androidx.compose.material3.FilterChip(
                        selected = selectedTag == tag,
                        onClick = { selectedTag = if (selectedTag == tag) "" else tag },
                        label = { Text(tag) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (canEdit) {
            var lastClickTime by rememberSaveable { mutableStateOf(0L) }
            val doubleClickThreshold = 300L
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Мои мероприятия", style = MaterialTheme.typography.titleMedium)
                FloatingActionButton(onClick = onAddEvent, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить мероприятие")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(events.filter {
                    (isAdmin || (isOrganizer && it.organizerId == currentUserId)) &&
                    it.title.contains(filterText, ignoreCase = true) &&
                    (selectedTag.isBlank() || it.tags.contains(selectedTag))
                }.toList()) { event ->
                    EventCard(
                        event = event,
                        onClick = { navController.navigate("event_details/${event.id}") },
                        cardWidth = androidx.compose.ui.unit.Dp.Unspecified
                    )
                }
            }
        } else {
            Text("Доступные мероприятия", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(events.filter {
                    it.title.contains(filterText, ignoreCase = true) &&
                    (selectedTag.isBlank() || it.tags.contains(selectedTag))
                }.toList()) { event ->
                    EventCard(
                        event = event,
                        onClick = { navController.navigate("event_details/${event.id}") },
                        cardWidth = androidx.compose.ui.unit.Dp.Unspecified
                    )
                }
            }
        }
    }
}
