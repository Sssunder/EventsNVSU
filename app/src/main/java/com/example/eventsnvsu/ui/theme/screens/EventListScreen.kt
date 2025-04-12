package com.example.eventsnvsu.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventsnvsu.data.FirebaseRepository
import com.example.eventsnvsu.model.Event

@Composable
fun EventListScreen(navController: NavController) {
    val context = LocalContext.current
    val events = remember { mutableStateOf<List<Event>>(emptyList()) }
    val repository = FirebaseRepository()

    LaunchedEffect(Unit) {
        repository.getEvents(
            onSuccess = { events.value = it },
            onFailure = { message -> Toast.makeText(context, message, Toast.LENGTH_LONG).show() }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Список мероприятий", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        events.value.forEach { event ->
            Text(text = "${event.title} - ${event.date}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventListScreenPreview() {
    MaterialTheme {
        EventListScreen(navController = NavController(LocalContext.current))
    }
}