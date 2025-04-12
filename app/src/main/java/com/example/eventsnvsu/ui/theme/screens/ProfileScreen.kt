package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController, isOrganizer: Boolean) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Профиль", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (isOrganizer) {
            Text("Роль: Организатор")
            Button(onClick = { /* настройки организатора */ }) {
                Text("Управление мероприятиями")
            }
        } else {
            Text("Роль: Участник")
            Button(onClick = { /* настройки пользователя */ }) {
                Text("Редактировать профиль")
            }
        }
    }
}
