package com.example.eventsnvsu.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.eventsnvsu.data.FirebaseRepository

class AuthViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    val currentUserRole = mutableStateOf("")

    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        repository.login(email, password,
            onSuccess = {
                repository.getUserRole(
                    onSuccess = { role ->
                        currentUserRole.value = role
                        onSuccess()
                    },
                    onFailure = onFailure
                )
            },
            onFailure = onFailure
        )
    }

    fun register(
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repository.register(email, password, role, onSuccess, onFailure)
    }
}