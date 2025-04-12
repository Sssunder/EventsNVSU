package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SearchScreen(navController: NavHostController, isOrganizer: Boolean) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Поиск мероприятий", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Фильтры поиска (заглушка)
        Text("Фильтры по дате, категории и т.п.")

        Spacer(modifier = Modifier.height(24.dp))

        if (isOrganizer) {
            Button(onClick = {
                // Переход к участникам мероприятия
            }) {
                Text("Просмотреть участников мероприятий")
            }
        }
    }
}
