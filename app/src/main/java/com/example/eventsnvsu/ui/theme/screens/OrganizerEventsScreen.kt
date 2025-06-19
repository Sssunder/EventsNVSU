package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.eventsnvsu.ui.theme.EventCard
import com.example.eventsnvsu.viewmodel.AuthViewModel
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerEventsScreen(navController: NavController, eventViewModel: EventViewModel = viewModel()) {
    val events = eventViewModel.events
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
    val now = java.util.Date()
    val upcoming = events.filter {
        it.organizerId == currentUserId && try {
            val eventDate = dateFormat.parse(it.date)
            eventDate != null && eventDate.after(now)
        } catch (e: Exception) { true }
    }
    val archived = events.filter {
        it.organizerId == currentUserId && try {
            val eventDate = dateFormat.parse(it.date)
            eventDate != null && eventDate.before(now)
        } catch (e: Exception) { false }
    }

    var lastClickTime by remember { mutableStateOf(0L) }
    val doubleClickThreshold = 300L

    LaunchedEffect(Unit) {
        eventViewModel.loadOrganizerEvents(currentUserId)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val authViewModel: AuthViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)
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
            items(upcoming, key = { it.id }) { event ->
                Column {
                    EventCard(
                        event = event,
                        cardWidth = androidx.compose.ui.unit.Dp.Unspecified,
                        onClick = {
                            val now = System.currentTimeMillis()
                            if (now - lastClickTime < doubleClickThreshold) {
                                navController.navigate("edit_event/${event.id}")
                            } else {
                                selectedEventId = event.id
                            }
                            lastClickTime = now
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                eventViewModel.deleteEvent(event.id,
                                    onSuccess = {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Мероприятие удалено")
                                        }
                                    },
                                    onFailure = { err ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Ошибка удаления: $err")
                                        }
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Удалить мероприятие")
                    }
                }
            }
            if (archived.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Архив мероприятий", style = MaterialTheme.typography.titleMedium)
                }
                items(archived, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        cardWidth = androidx.compose.ui.unit.Dp.Unspecified,
                        onClick = {
                            navController.navigate("event_details/${event.id}")
                        }
                    )
                }
            }
        }
        // --- Модальное окно с деталями мероприятия ---
        if (selectedEventId != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedEventId = null },
                sheetState = sheetState,
                containerColor = Color.Transparent,
                scrimColor = Color.Black.copy(alpha = 0.45f)
            ) {
                EventDetailScreenWrapper(
                    navController = navController as NavHostController,
                    eventId = selectedEventId,
                    isOrganizer = true,
                    authViewModel = authViewModel,
                    eventViewModel = eventViewModel,
                    onEditEvent = {
                        selectedEventId?.let { navController.navigate("edit_event/$it") }
                    },
                    onAddEvent = {}
                )
            }
        }
    }
}
