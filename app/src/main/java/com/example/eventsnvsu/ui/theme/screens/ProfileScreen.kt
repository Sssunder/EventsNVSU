package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eventsnvsu.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf(authViewModel.currentUser?.email ?: "") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }
    val role = when (authViewModel.currentUserRole.value) {
        "admin" -> "Администратор"
        "organizer" -> "Организатор"
        "participant" -> "Участник"
        else -> "Не определена"
    }

    // Для будущей аватарки (заглушка)
    val avatarInitials = remember {
        val n = name.ifBlank { email.take(1).uppercase() }
        if (n.isNotBlank()) n.take(2).uppercase() else "?"
    }

    LaunchedEffect(authViewModel.currentUser) {
        // Можно подгрузить имя из Firestore, если оно есть
        authViewModel.refreshUser()
        // name = ... (если реализовано хранение имени)
        email = authViewModel.currentUser?.email ?: ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Аватарка (заглушка)
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
                // В будущем можно заменить на Image(...)
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Имя и email
            Text(
                text = name.ifBlank { "Имя пользователя" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Роль
            Text(
                text = "Роль: $role",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
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
                                snackbarHostState.showSnackbar("Профиль обновлен")
                            }
                        },
                        onFailure = { error: String  ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        }
                    )
                }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Сохранить")
                }
            } else {
                Button(
                    onClick = { editMode = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Редактировать профиль")
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Кнопка выхода
            Button(
                onClick = {
                    authViewModel.logout {
                        navController.navigate(com.example.eventsnvsu.navigation.Screen.Login.route) {
                            popUpTo(com.example.eventsnvsu.navigation.Screen.Main.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Выйти", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Выйти", color = Color.White)
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
