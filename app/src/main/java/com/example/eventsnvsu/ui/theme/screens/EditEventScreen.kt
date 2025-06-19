package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eventsnvsu.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: String?,
    eventViewModel: EventViewModel = viewModel()
) {
    val context = LocalContext.current
    val event = eventViewModel.events.find { it.id == eventId }
    val isLoading = event == null
    androidx.compose.runtime.LaunchedEffect(eventId) {
        if (event == null && eventId != null) {
            eventViewModel.observeAllEvents()
        }
    }
    if (isLoading) {
        Text("Загрузка мероприятия...", modifier = Modifier.padding(24.dp))
        return
    }
    val title = remember { mutableStateOf(event.title) }
    val description = remember { mutableStateOf(event.description) }
    val location = remember { mutableStateOf(event.location) }
    val date = remember { mutableStateOf(event.date) }
    val tags = remember { mutableStateOf(event.tags.joinToString(", ")) }
    val organizerId = event.organizerId

    // DatePicker
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    if (showDatePicker.value) {
        DatePickerDialog(onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                Button(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        date.value = dateFormat.format(Date(millis))
                    }
                    showDatePicker.value = false
                }) { Text("Ок") }
            },
            dismissButton = {
                Button(onClick = { showDatePicker.value = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val chatLink = remember { mutableStateOf(event.chatLink ?: "") }

    Column(Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        OutlinedTextField(
            value = location.value,
            onValueChange = { location.value = it },
            label = { Text("Место") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 2,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        OutlinedTextField(
            value = date.value,
            onValueChange = { date.value = it },
            label = { Text("Дата") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                Text(
                    "\uD83D\uDCC5",
                    modifier = Modifier.clickable { showDatePicker.value = true }
                )
            }
        )
        OutlinedTextField(
            value = tags.value,
            onValueChange = { tags.value = it },
            label = { Text("Теги (через запятую)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 2,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        OutlinedTextField(
            value = chatLink.value,
            onValueChange = { chatLink.value = it },
            label = { Text("Ссылка на чат (например, Telegram)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        Button(onClick = {
            val updatedEvent = event.copy(
                title = title.value,
                description = description.value,
                location = location.value,
                date = date.value,
                tags = tags.value.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                chatLink = chatLink.value
                // imageUrl не обновляется, только локально
            )
            eventViewModel.createOrUpdateEvent(updatedEvent,
                onSuccess = { navController.popBackStack() },
                onFailure = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Сохранить изменения")
        }
    }
}
