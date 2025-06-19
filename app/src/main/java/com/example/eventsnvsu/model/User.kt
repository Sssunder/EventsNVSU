// Файл: app/src/main/java/com/example/eventsnvsu/model/User.kt
package com.example.eventsnvsu.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "organizer",
    val photoUrl: String? = null // Новое поле для фото профиля
) {
    // Обязательный пустой конструктор для Firebase
    constructor() : this("", "", "organizer")
}