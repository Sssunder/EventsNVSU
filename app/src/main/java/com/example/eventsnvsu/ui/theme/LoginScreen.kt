package com.example.eventsnvsu.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventsnvsu.R
import com.example.eventsnvsu.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current // Получаем контекст здесь
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
                text = "Добро пожаловать!",
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

            // Кнопка для входа
            Button(
                onClick = {
                    // Здесь вы можете добавить код для отправки данных на сервер или сохранения их локально
                    Toast.makeText(context, "Вход успешен", Toast.LENGTH_SHORT).show()
                    navController.navigate("event_list_screen") // Перенаправление
                },
                shape = RoundedCornerShape(16.dp), // Скругленные углы
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Высота кнопки
                    .padding(top = 16.dp), // Отступ сверху
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Войти", style = MaterialTheme.typography.bodyLarge)
            }

            // Дополнительная информация или кнопка для регистрации
            TextButton(onClick = {
                // Логика для регистрации или другого действия
                navController.navigate("registration_screen") // Перенаправление на экран регистрации
            }) {
                Text(
                    text = "Создать аккаунт",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        navController = NavController(context = LocalContext.current),
        authViewModel = AuthViewModel() // Мок для preview
    )
}
