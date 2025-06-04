package com.example.eventsnvsu.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventsnvsu.data.FirebaseRepository
import com.example.eventsnvsu.model.Event
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    val events = mutableStateListOf<Event>()
    private var eventsListener: ListenerRegistration? = null

    fun loadAllEvents() {
        viewModelScope.launch {
            repository.getEvents(
                onSuccess = { events.clear(); events.addAll(it) },
                onFailure = { /* Handle error */ }
            )
        }
    }

    fun loadOrganizerEvents(organizerId: String) {
        viewModelScope.launch {
            repository.getOrganizerEvents(organizerId,
                onSuccess = { events.clear(); events.addAll(it) },
                onFailure = { /* Handle error */ }
            )
        }
    }

    fun registerForEvent(eventId: String) {
        viewModelScope.launch {
            repository.registerForEvent(eventId,
                onSuccess = { /* Update UI */ },
                onFailure = { /* Handle error */ }
            )
        }
    }

    fun observeAllEvents() {
        eventsListener?.remove()
        eventsListener = repository.observeEvents(
            onEventsChanged = { newEvents ->
                events.clear()
                events.addAll(newEvents)
            },
            onError = { /* Handle error */ }
        )
    }

    fun createOrUpdateEvent(event: Event, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (event.id.isEmpty()) {
            repository.createEvent(event, onSuccess, onFailure)
        } else {
            repository.updateEvent(event, onSuccess, onFailure)
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventsListener?.remove()
    }
}

