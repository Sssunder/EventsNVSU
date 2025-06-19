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
        val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (event.id.isEmpty()) {
            // При создании события всегда подставляем organizerId текущего пользователя
            val eventWithOrganizer = event.copy(organizerId = currentUid ?: "")
            repository.createEvent(eventWithOrganizer, onSuccess, onFailure)
        } else {
            repository.updateEvent(event, onSuccess, onFailure)
        }
    }

    fun followEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.followEvent(eventId, onSuccess, onFailure)
        }
    }

    fun unfollowEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.unfollowEvent(eventId, onSuccess, onFailure)
        }
    }

    fun deleteEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.deleteEvent(eventId,
                onSuccess = {
                    // После удаления обновляем список
                    loadAllEvents()
                    onSuccess()
                },
                onFailure = { onFailure(it) }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventsListener?.remove()
    }
}
