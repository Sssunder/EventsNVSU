package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eventsnvsu.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, isOrganizer: Boolean, authViewModel: AuthViewModel = viewModel()) {
    val snackbarHostState = rememberSnackbarHostState()
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Профиль", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        if (editMode) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Button(onClick = {
                authViewModel.updateProfile(
                    newEmail = email,
                    newPassword = password,
                    newName = name,
                    onSuccess = {
                        editMode = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("П��офиль обновлен")
                        }
                    },
                    onFailure = { error ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(error)
                        }
                    }
                )
            }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Сохранить")
            }
        } else {
            Text("Имя: $name")
            Text("Email: $email")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { editMode = true }) {
                Text("Редактировать профиль")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (isOrganizer) {
            Text("Роль: Организатор")
            Button(onClick = { navController.navigate("organizer_events") }) {
                Text("Мои мероприятия")
            }
        } else {
            Text("Роль: Участник")
        }
    }
}
