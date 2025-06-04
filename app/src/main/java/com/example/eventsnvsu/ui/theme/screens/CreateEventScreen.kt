package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavController) {
    val context = LocalContext.current
    val eventViewModel: EventViewModel = viewModel()
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val tags = remember { mutableStateOf("") }

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

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default)
        )
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = location.value,
                onValueChange = { location.value = it },
                label = { Text("Место") },
                modifier = Modifier.weight(1f),
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default)
            )
            Spacer(Modifier.padding(4.dp))
            Button(onClick = {
                // TODO: интеграция с Яндекс.Картами
                Toast.makeText(context, "Выбор места на карте пока не реализован", Toast.LENGTH_SHORT).show()
            }, modifier = Modifier.alignByBaseline()) {
                Text("Выбрать на карте")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker.value = true }
        ) {
            OutlinedTextField(
                value = date.value,
                onValueChange = {},
                label = { Text("Дата") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                maxLines = 1
            )
        }
        OutlinedTextField(
            value = tags.value,
            onValueChange = { tags.value = it },
            label = { Text("Теги (через запятую)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 2,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default)
        )
        Spacer(Modifier.padding(8.dp))
        Button(onClick = {
            val event = Event(
                title = title.value,
                description = description.value,
                location = location.value,
                date = date.value,
                organizerId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: "",
                tags = tags.value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            )
            eventViewModel.createOrUpdateEvent(event,
                onSuccess = { navController.popBackStack() },
                onFailure = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Создать мероприятие")
        }
    }
}

