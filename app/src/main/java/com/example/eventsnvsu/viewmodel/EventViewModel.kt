package com.example.eventsnvsu.viewmodel

import FirebaseRepository
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsnvsu.model.Event
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    val events = mutableStateListOf<Event>()

    fun loadAllEvents() {
        repository.getEvents(
            onSuccess = { events.clear(); events.addAll(it) },
            onFailure = { /* Handle error */ }
        )
    }

    fun loadOrganizerEvents(organizerId: String) {
        repository.getOrganizerEvents(organizerId,
            onSuccess = { events.clear(); events.addAll(it) },
            onFailure = { /* Handle error */ }
        )
    }

    fun registerForEvent(eventId: String) {
        repository.registerForEvent(eventId,
            onSuccess = { /* Update UI */ },
            onFailure = { /* Handle error */ }
        )
    }
}
