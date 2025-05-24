package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventsnvsu.model.Event
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EventDetailScreen(
    navController: NavController,
    event: Event,
    isOrganizer: Boolean = false,
    onEdit: (() -> Unit)? = null,
    onRegister: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 12.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(event.title, style = MaterialTheme.typography.headlineLarge)
                Text(event.date, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(event.location, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(event.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                if (isOrganizer && onEdit != null) {
                    Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                        Text("Редактировать")
                    }
                } else if (!isOrganizer && onRegister != null) {
                    Button(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
                        Text("Записаться")
                    }
                }
            }
        }
    }
}

