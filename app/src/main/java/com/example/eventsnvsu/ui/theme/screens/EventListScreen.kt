package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.eventsnvsu.ui.theme.BigEventCard
import com.example.eventsnvsu.viewmodel.AuthViewModel
import com.example.eventsnvsu.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController,
    eventViewModel: EventViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel() // добавим для передачи в EventDetailScreen
) {
    val events = eventViewModel.events
    LaunchedEffect(Unit) {
        eventViewModel.observeAllEvents()
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth: Dp = screenWidth // теперь карточка на всю ширину
    val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
    val now = java.util.Date()
    val upcomingEvents = events.filter {
        try {
            val eventDate = dateFormat.parse(it.date)
            eventDate != null && eventDate.after(now)
        } catch (e: Exception) { true }
    }
    val pagerState = rememberPagerState(pageCount = { upcomingEvents.size })

    // --- Новое: состояние для выбранного события и модального окна ---
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Градиентный фон с блюром
        Canvas(modifier = Modifier.matchParentSize().blur(24.dp)) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF165DAC), Color(0xFF36D1DC)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                ),
                size = size
            )
        }
        if (upcomingEvents.isNotEmpty()) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxHeight()) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BigEventCard(
                        event = upcomingEvents[page],
                        onClick = { selectedEventId = upcomingEvents[page].id },
                        cardWidth = cardWidth
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.material3.Text("Нет мероприятий", color = Color.White.copy(alpha = 0.7f))
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
                    isOrganizer = false, // для списка событий всегда участник
                    authViewModel = authViewModel,
                    eventViewModel = eventViewModel,
                    onEditEvent = {},
                    onAddEvent = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventListScreenPreview() {
    val mockNav = rememberNavController()
    EventListScreen(navController = mockNav)
}
