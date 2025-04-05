package com.example.eventsnvsu.ui.theme

import FirebaseRepository
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventsnvsu.navigation.Screen

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val repository = FirebaseRepository()
    val userData = remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        repository.getUserProfile(auth.currentUser?.uid ?: "") { user ->
            userData.value = user
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        userData.value?.let { user ->
            Text("Email: ${user.email}", style = MaterialTheme.typography.headlineSmall)
            Text("Роль: ${user.role}", style = MaterialTheme.typography.bodyMedium)
        }

        Button(
            onClick = {
                auth.signOut()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        ) {
            Text("Выйти")
        }
    }
}