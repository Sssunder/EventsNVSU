package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.example.eventsnvsu.ui.theme.EventCard

@Composable
fun EventListScreen(navController: NavController, eventViewModel: EventViewModel = viewModel()) {
    val events = eventViewModel.events
    LaunchedEffect(Unit) {
        eventViewModel.observeAllEvents()
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth: Dp = 300.dp
    val horizontalPadding = ((screenWidth - cardWidth) / 2).coerceAtLeast(0.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Список мероприятий",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(events.toList()) { event ->
                    EventCard(event = event) {
                        navController.navigate("event_details/${event.id}")
                    }
                }
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
