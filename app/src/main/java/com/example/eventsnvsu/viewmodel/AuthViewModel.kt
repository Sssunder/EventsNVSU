package com.example.eventsnvsu.viewmodel

import FirebaseRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

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