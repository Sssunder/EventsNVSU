package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: String?,
    eventViewModel: EventViewModel = viewModel()
) {
    val context = LocalContext.current
    val event = eventViewModel.events.find { it.id == eventId }
    val isEdit = event != null
    val title = remember { mutableStateOf(event?.title ?: "") }
    val description = remember { mutableStateOf(event?.description ?: "") }
    val location = remember { mutableStateOf(event?.location ?: "") }
    val date = remember { mutableStateOf(event?.date ?: "") }
    val organizerId = event?.organizerId ?: FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    LaunchedEffect(eventId) {
        eventViewModel.observeAllEvents()
    }

    Column(Modifier.padding(16.dp)) {
        Text(if (isEdit) "Редактировать мероприятие" else "Создать мероприятие")
        Spacer(Modifier.padding(8.dp))
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location.value,
            onValueChange = { location.value = it },
            label = { Text("Место") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = date.value,
            onValueChange = { date.value = it },
            label = { Text("Дата") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.padding(8.dp))
        Button(onClick = {
            val newEvent = Event(
                id = event?.id, // если редактирование — сохраняем id
                title = title.value,
                description = description.value,
                location = location.value,
                date = date.value,
                organizerId = organizerId
            )
            eventViewModel.createOrUpdateEvent(newEvent,
                onSuccess = { navController.popBackStack() },
                onFailure = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            )
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Сохранить")
        }
    }
}

