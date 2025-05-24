package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavHostController,
    isOrganizer: Boolean,
    eventViewModel: EventViewModel,
    onEditEvent: (Event) -> Unit,
    onAddEvent: () -> Unit
) {
    val events = eventViewModel.events
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Поиск мероприятий", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        // Фильтры поиска (заглушка)
        Text("Фильтры по дате, категории и т.п.")
        Spacer(modifier = Modifier.height(24.dp))
        if (isOrganizer) {
            var lastClickTime by remember { mutableStateOf(0L) }
            val doubleClickThreshold = 300L
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Мои мероприятия", style = MaterialTheme.typography.titleMedium)
                FloatingActionButton(onClick = onAddEvent, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить мероприятие")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(events.filter { it.organizerId == currentUserId }.toList()) { event ->
                    Row(Modifier
                        .fillMaxWidth()
                        .clickable {
                            val now = System.currentTimeMillis()
                            if (now - lastClickTime < doubleClickThreshold) {
                                navController.navigate("edit_event/${event.id}")
                            } else {
                                navController.navigate("event_details/${event.id}")
                            }
                            lastClickTime = now
                        }
                        .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(event.title)
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать", modifier = Modifier.clickable {
                            navController.navigate("edit_event/${event.id}")
                        })
                    }
                }
            }
        } else {
            Text("Доступные мероприятия", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(events.toList()) { event ->
                    Text(event.title, Modifier
                        .padding(8.dp)
                        .clickable { navController.navigate("event_details/${event.id}") })
                }
            }
        }
    }
}
