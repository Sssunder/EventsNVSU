package com.example.eventsnvsu.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val date: String = "",
    val organizerId: String = "",
    val tags: List<String> = emptyList(), // Новое поле для тегов/факультета
    val imageUrl: String? = null, // Новое поле для фото
    val followers: List<String> = emptyList(), // Новый список подписчиков
    val chatLink: String? = null // Новое поле для ссылки на чат
) {
    constructor() : this("", "", "", "", "", "", emptyList(), null, emptyList(), null)
}
