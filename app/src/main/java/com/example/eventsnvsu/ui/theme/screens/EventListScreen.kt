package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventsnvsu.data.FirebaseRepository
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.ui.theme.EventCard

@Composable
fun EventListScreen(navController: NavController) {
    val context = LocalContext.current
    val events = remember { mutableStateOf<List<Event>>(emptyList()) }
    val repository = remember { FirebaseRepository() }

    LaunchedEffect(Unit) {
        repository.getEvents(
            onSuccess = { events.value = it },
            onFailure = { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        )
    }

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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(events.value) { event ->
                EventCard(event = event)
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
