package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEventsScreen(navController: NavController, eventViewModel: EventViewModel = viewModel()) {
    val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val events = eventViewModel.events.filter { it.followers.contains(currentUserId) }
    val (upcoming, archived) = remember(events) {
        val now = Date() // теперь вычисляется при каждом recomposition
        events.partition { event ->
            try {
                val eventDate = dateFormat.parse(event.date)
                eventDate != null && eventDate.after(now)
            } catch (e: Exception) { true }
        }
    }
    LaunchedEffect(Unit) {
        eventViewModel.observeAllEvents()
    }
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val authViewModel: AuthViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // убираем градиент, делаем белый фон
            .padding(16.dp)
    ) {
        Text("Мои мероприятия", style = MaterialTheme.typography.headlineSmall)
        androidx.compose.foundation.lazy.LazyColumn {
            if (upcoming.isNotEmpty()) {
                items(upcoming) { event ->
                    EventCard(
                        event = event,
                        onClick = { selectedEventId = event.id },
                        cardWidth = androidx.compose.ui.unit.Dp.Unspecified
                    )
                }
            } else {
                item { Text("Нет предстоящих мероприятий", color = Color.Gray, modifier = Modifier.padding(8.dp)) }
            }
            if (archived.isNotEmpty()) {
                item { Text("Архив мероприятий", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp)) }
                items(archived) { event ->
                    EventCard(
                        event = event,
                        onClick = { selectedEventId = event.id },
                        cardWidth = androidx.compose.ui.unit.Dp.Unspecified
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
                isOrganizer = false,
                authViewModel = authViewModel,
                eventViewModel = eventViewModel,
                onEditEvent = {},
                onAddEvent = {}
            )
        }
    }
}}
