package com.example.eventsnvsu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.eventsnvsu.navigation.AppNavigation
import com.example.eventsnvsu.navigation.MainNavigationScaffold
import com.example.eventsnvsu.ui.theme.EventsNVSUTheme
import com.example.eventsnvsu.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val navController = rememberNavController()
            val isOrganizer = false // или true, в зависимости от того, что вы хотите протестировать
            EventsNVSUTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}
