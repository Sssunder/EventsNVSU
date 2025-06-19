package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    val chatLink = remember { mutableStateOf("") }

    // DatePicker
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    // Карусель шагов
    val steps = listOf("Название", "Описание", "Место", "Дата", "Теги", "Ссылка на чат")
    val stepCount = steps.size
    val currentStep = remember { mutableStateOf(0) }
    val isUploading = remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
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

    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Шаг ${currentStep.value + 1} из $stepCount", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.padding(8.dp))
        when (currentStep.value) {
            0 -> OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            1 -> OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            2 -> OutlinedTextField(
                value = location.value,
                onValueChange = { location.value = it },
                label = { Text("Место (TODO: интеграция с Яндекс.Картами)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            3 -> OutlinedTextField(
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
            4 -> OutlinedTextField(
                value = tags.value,
                onValueChange = { tags.value = it },
                label = { Text("Теги (через запятую)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            5 -> OutlinedTextField(
                value = chatLink.value,
                onValueChange = { chatLink.value = it },
                label = { Text("Ссылка на чат (например, Telegram)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        }
        Spacer(Modifier.padding(8.dp))
        Row(Modifier.fillMaxWidth()) {
            if (currentStep.value > 0) {
                Button(onClick = { currentStep.value-- }, modifier = Modifier.weight(1f)) {
                    Text("Назад")
                }
                Spacer(Modifier.padding(4.dp))
            }
            if (currentStep.value < stepCount - 1) {
                Button(onClick = { currentStep.value++ }, modifier = Modifier.weight(1f)) {
                    Text("Далее")
                }
            } else {
                Button(onClick = {
                    isUploading.value = true
                    val organizerId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
                    val event = Event(
                        title = title.value,
                        description = description.value,
                        location = location.value,
                        date = date.value,
                        tags = tags.value.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                        imageUrl = null, // Фото больше не добавляется
                        chatLink = chatLink.value,
                        organizerId = organizerId,
                        followers = listOf(organizerId) // Организатор сразу в подписчиках
                    )
                    eventViewModel.createOrUpdateEvent(event,
                        onSuccess = { isUploading.value = false; navController.popBackStack() },
                        onFailure = { isUploading.value = false; Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                    )
                }, modifier = Modifier.weight(1f), enabled = !isUploading.value) {
                    Text(if (isUploading.value) "Загрузка..." else "Создать мероприятие")
                }
            }
        }
        if (currentStep.value == stepCount - 1) {
            Spacer(Modifier.padding(8.dp))
            Text("Проверьте данные:", style = MaterialTheme.typography.titleSmall)
            Text("Название: ${title.value}")
            Text("Описание: ${description.value}")
            Text("Место: ${location.value}")
            Text("Дата: ${date.value}")
            Text("Теги: ${tags.value}")
        }
    }
}
