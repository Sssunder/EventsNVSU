// Файл: app/src/main/java/com/example/eventsnvsu/model/User.kt
package com.example.eventsnvsu.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String = "",
    val email: String = "",
    val role: String = "user"
) {
    // Обязательный пустой конструктор для Firebase
    constructor() : this("", "", "user")
}