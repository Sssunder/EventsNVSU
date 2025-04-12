package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventsnvsu.R
import com.example.eventsnvsu.navigation.Screen
import com.example.eventsnvsu.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val selectedRole = remember { mutableStateOf("user") }
    // Контейнер с фоном и центрированным контентом
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Добавляем фон
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp), // Добавляем отступы от краев
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Логотип или изображение
            Image(
                painter = painterResource(id = R.drawable.ic_login), // Здесь укажите вашу иконку
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp) // Размер изображения
                    .padding(bottom = 40.dp) // Отступ снизу
            )

            // Текст заголовка
            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Поля для ввода имени пользователя и пароля
            val username = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }

            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Имя пользователя") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Row(Modifier.padding(vertical = 8.dp)) {
                RadioButton(
                    selected = selectedRole.value == "user",
                    onClick = { selectedRole.value = "user" }
                )
                Text("Участник", Modifier.align(Alignment.CenterVertically))

                Spacer(Modifier.width(16.dp))

                RadioButton(
                    selected = selectedRole.value == "organizer",
                    onClick = { selectedRole.value = "organizer" }
                )
                Text("Организатор", Modifier.align(Alignment.CenterVertically))
            }
            // Кнопка для регистрации
            Button(onClick = {
                authViewModel.register(
                    email.value,
                    password.value,
                    selectedRole.value,
                    onSuccess = { navController.navigate(Screen.Login.route) },
                    onFailure = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            }) {
                Text("Зарегистрироваться")
            }

            // Дополнительная информация или кнопка для входа
            TextButton(onClick = {
                // Логика для входа или другого действия
                navController.navigate("login_screen") // Перенаправление на экран входа
            }) {
                Text(
                    text = "Уже есть аккаунт? " +
                            "\nВойти",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center // Выравнивание текста по центру
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    val navController = rememberNavController()
    val authViewModel = AuthViewModel() // Создайте экземпляр AuthViewModel здесь
    RegistrationScreen(navController, authViewModel)
}


