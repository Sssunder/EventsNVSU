package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.eventsnvsu.data.FirebaseRepository
import com.example.eventsnvsu.model.Event
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreateEventScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = FirebaseRepository()
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    Column {
        OutlinedTextField(value = title.value, onValueChange = { title.value = it }, label = { Text("Название") })
        OutlinedTextField(value = description.value, onValueChange = { description.value = it }, label = { Text("Описание") })
        OutlinedTextField(value = location.value, onValueChange = { location.value = it }, label = { Text("Место") })
        OutlinedTextField(value = date.value, onValueChange = { date.value = it }, label = { Text("Дата") })

        Button(onClick = {
            val event = Event(
                title = title.value,
                description = description.value,
                location = location.value,
                date = date.value,
                organizerId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )

            repository.createEvent(event,
                onSuccess = { navController.popBackStack() },
                onFailure = {
                    val it = null
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }) {
            Text("Создать мероприятие")
        }
    }
}