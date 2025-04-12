package com.example.eventsnvsu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.eventsnvsu.navigation.MainNavigationScaffold
import com.example.eventsnvsu.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel = AuthViewModel() // или через viewModel() позже

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainNavigationScaffold(
                    isOrganizer = true, // временно вручную
                    authViewModel = authViewModel
                )
            }
        }
    }
}
