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
    val participants: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "", "", emptyList())
}