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

    var pendingRegistrationEmail by mutableStateOf<String?>(null)
    var pendingRegistrationPassword by mutableStateOf<String?>(null)

    val isEmailVerified: Boolean
        get() = currentUser?.isEmailVerified == true

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
        val cleanEmail = email.trim()
        val cleanPassword = password.trim()
        repository.login(cleanEmail, cleanPassword,
            onSuccess = {
                currentUser = repository.currentUser
                repository.getUserRole(
                    onSuccess = { role ->
                        currentUserRole.value = role
                        isLoading = false
                        onSuccess()
                    },
                    onFailure = {
                        errorMessage = it
                        isLoading = false
                        onSuccess()
                    }
                )
            },
            onFailure = {
                // Показываем более дружелюбное сообщение
                errorMessage = if (it.contains("auth credential is incorrect", true) || it.contains("password is invalid", true)) {
                    "Неверный email или пароль"
                } else {
                    it
                }
                isLoading = false
            }
        )
    }

    fun register(
        email: String,
        password: String,
        name: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        isLoading = true
        repository.register(email, password, name, role,
            onSuccess = {
                // После успешной регистрации сразу делаем logout, чтобы не логинило без подтверждения
                repository.signOut()
                currentUser = null
                currentUserRole.value = ""
                pendingRegistrationEmail = email
                pendingRegistrationPassword = password
                isLoading = false
                onSuccess()
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
        newEmail: String,
        newPassword: String,
        newName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repository.updateProfile(newEmail, newPassword, newName, onSuccess, onFailure)
    }

    fun loadCurrentUserName(onLoaded: (String) -> Unit) {
        val user = repository.currentUser
        if (user != null) {
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("users").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    onLoaded(doc.getString("name") ?: "")
                }
        }
    }

    fun uploadProfilePhoto(
        uri: android.net.Uri,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        repository.uploadProfilePhoto(uri, onSuccess, onFailure)
    }

    fun tryLoginAfterVerification(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val email = pendingRegistrationEmail
        val password = pendingRegistrationPassword
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            onFailure("Нет данных для входа. Попробуйте войти вручную.")
            return
        }
        login(email, password, onSuccess = {
            val user = currentUser
            if (user != null && user.isEmailVerified) {
                pendingRegistrationEmail = null
                pendingRegistrationPassword = null
                onSuccess()
            } else {
                logout {}
                onFailure("Email ещё не подтверждён. Проверьте почту.")
            }
        })
    }
}
