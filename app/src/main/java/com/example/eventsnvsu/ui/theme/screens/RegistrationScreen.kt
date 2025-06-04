package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
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
    val focusManager = LocalFocusManager.current
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

            // Поля для ввода email, имени пользователя и пароля
            val email = remember { mutableStateOf("") }
            val username = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }
            val emailFocusRequester = remember { FocusRequester() }
            val usernameFocusRequester = remember { FocusRequester() }
            val passwordFocusRequester = remember { FocusRequester() }

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    usernameFocusRequester.requestFocus()
                })
            )
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Имя пользователя") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(usernameFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    passwordFocusRequester.requestFocus()
                })
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
            // Кнопка для регистрации
            Button(onClick = {
                authViewModel.register(
                    email.value,
                    password.value,
                    "user", // всегда обычный пользователь
                    onSuccess = {
                        Toast.makeText(context, "Аккаунт успешно создан", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Login.route)
                    },
                    onFailure = { error: String ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            }) {
                Text("Зарегистрироваться")
            }

            // Дополнительная информация или кнопка для входа
            TextButton(onClick = {
                // Логика для входа или другого действия
                navController.navigate("login") // Перенаправление на экран входа
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
    val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    RegistrationScreen(navController, authViewModel)
}

