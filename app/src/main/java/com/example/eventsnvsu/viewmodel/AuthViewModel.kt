package com.example.eventsnvsu.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eventsnvsu.data.FirebaseRepository
import com.google.firebase.auth.FirebaseUser


class AuthViewModel : ViewModel() { // Конструктор без параметров
    private val repository = FirebaseRepository()

    var currentUser by mutableStateOf<FirebaseUser?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val currentUserRole = mutableStateOf("") // Оставим это для роли

    fun refreshUser() {
        currentUser = repository.currentUser
        if (currentUser != null) {
            repository.getUserRole(
                onSuccess = { role -> currentUserRole.value = role },
                onFailure = { /* Обработка ошибки загрузки роли */ }
            )
        } else {
            currentUserRole.value = ""
        }
    }

    init {
        refreshUser()
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        isLoading = true
        repository.login(email, password,
            onSuccess = {
                currentUser = repository.currentUser // О��новляем currentUser после успешного логина
                repository.getUserRole( // Загружаем роль после успешного логина
                    onSuccess = { role ->
                        currentUserRole.value = role
                        isLoading = false
                        onSuccess()
                    },
                    onFailure = {
                        errorMessage = it // Если загрузка роли не удалась
                        isLoading = false
                        onSuccess() // Все равно считаем логин успешным, но без роли
                    }
                )
            },
            onFailure = {
                errorMessage = it
                isLoading = false
            }
        )
    }

    fun register(
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        isLoading = true
        repository.register(email, password, role,
            onSuccess = {
                currentUser = repository.currentUser
                repository.getUserRole(
                    onSuccess = { userRole ->
                        currentUserRole.value = userRole
                        isLoading = false
                        onSuccess()
                    },
                    onFailure = { err ->
                        errorMessage = err
                        isLoading = false
                        onSuccess() // регистрация успешна, но роль не получена
                    }
                )
            },
            onFailure = { err ->
                errorMessage = err
                isLoading = false
                onFailure(err)
            }
        )
    }

    fun logout(onLogout: () -> Unit) {
        repository.signOut() // Используем публичную функцию signOut из репозитория
        currentUser = null // Обнуляем currentUser в ViewModel
        currentUserRole.value = "" // Обнуляем роль
        onLogout()
    }

    fun clearErrorMessage() {
        errorMessage = null
    }

    fun updateProfile(
        newEmail: kotlin.String,
        newPassword: kotlin.String,
        newName: kotlin.String,
        onSuccess: () -> kotlinx.coroutines.Job,
        onFailure: Any
    ) {
    }
}
