package com.example.eventsnvsu.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }
    var showUserEvents by remember { mutableStateOf(false) }
    val role = when (authViewModel.currentUserRole.value) {
        "admin" -> "Администратор"
        "organizer" -> "Организатор"
        "user" -> "Участник"
        else -> "Не определена"
    }

    // Для будущей аватарки (заглушка)
    val avatarInitials = remember {
        val n = name.ifBlank { email.take(1).uppercase() }
        if (n.isNotBlank()) n.take(2).uppercase() else "?"
    }

    var photoUrl by remember { mutableStateOf(authViewModel.currentUser?.photoUrl ?: "") }
    LaunchedEffect(authViewModel.currentUser) {
        authViewModel.refreshUser()
        authViewModel.loadCurrentUserName { loadedName ->
            name = loadedName
        }
        email = authViewModel.currentUser?.email ?: ""
        // Загружаем photoUrl из Firestore
        val user = authViewModel.currentUser
        if (user != null) {
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("users").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    val url = doc.getString("photoUrl") ?: ""
                    photoUrl = url
                }
        }
    }

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
        // Только выбор иконки, не загружаем и не сохраняем фото
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Размытый фон eventlogo
        Image(
            painter = painterResource(id = com.example.eventsnvsu.R.drawable.eventlogo),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .blur(32.dp),
            alpha = 0.25f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Логотип без круга, с обрезкой по центру и плавным скруглением
            Image(
                painter = painterResource(id = com.example.eventsnvsu.R.drawable.eventlogo),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(240.dp) // увеличен размер логотипа
                    .clip(RoundedCornerShape(32.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Имя и email
            Text(
                text = name.ifBlank { "ФИО" },
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
                    label = { Text("ФИО") },
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
            // Кнопка просмотра ближайших мероприятий
            Button(
                onClick = {
                    navController.navigate("user_events")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Archive, contentDescription = "Архив мероприятий", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Мои мероприятия", color = Color.White)
            }
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
