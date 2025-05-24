package com.example.eventsnvsu.data

import com.example.eventsnvsu.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirebaseRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun register(email: String, password: String, role: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userRole = hashMapOf("role" to role)
                    db.collection("users").document(user?.uid ?: "").set(userRole)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e.message ?: "Unknown error")
                        }
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun getUserRole(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        db.collection("users").document(user?.uid ?: "").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val role = document.getString("role") ?: ""
                    onSuccess(role)
                } else {
                    onFailure("User not found")
                }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Unknown error")
            }
    }

    fun getEvents(onSuccess: (List<Event>) -> Unit, onFailure: (String) -> Unit) {
        db.collection("events").get()
            .addOnSuccessListener { querySnapshot ->
                val events = querySnapshot.toObjects(Event::class.java)
                onSuccess(events)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка загрузки мероприятий")
            }
    }

    fun createEvent(event: Event, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("events").add(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка создания мероприятия")
            }
    }

    fun updateEvent(event: Event, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (event.id.isNullOrEmpty()) {
            onFailure("Некорректный id мероприятия")
            return
        }
        db.collection("events").document(event.id!!).set(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка обновления мероприятия")
            }
    }

    fun getOrganizerEvents(organizerId: String, onSuccess: (List<Event>) -> Unit, onFailure: (String) -> Unit) {
        db.collection("events").whereEqualTo("organizerId", organizerId).get()
            .addOnSuccessListener { result ->
                val events = result.map { document ->
                    document.toObject(Event::class.java)
                }
                onSuccess(events)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Unknown error")
            }
    }

    fun registerForEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        db.collection("events").document(eventId).update("participants", FieldValue.arrayUnion(user?.uid))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Unknown error")
            }
    }

    fun observeEvents(
        onEventsChanged: (List<Event>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        return db.collection("events")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e.message ?: "Ошибка слушателя мероприятий")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val events = snapshot.toObjects(Event::class.java)
                    onEventsChanged(events)
                }
            }
    }

    fun updateProfile(
        newEmail: String?,
        newPassword: String?,
        newName: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onFailure("Пользователь не найден")
            return
        }
        // Обновление email
        if (!newEmail.isNullOrBlank() && newEmail != user.email) {
            user.updateEmail(newEmail)
                .addOnFailureListener { onFailure(it.message ?: "Ошибка обновления email") }
        }
        // Обновление пароля
        if (!newPassword.isNullOrBlank()) {
            user.updatePassword(newPassword)
                .addOnFailureListener { onFailure(it.message ?: "Ошибка обновления пароля") }
        }
        // Обновление имени в Firestore
        if (!newName.isNullOrBlank()) {
            db.collection("users").document(user.uid).update("name", newName)
                .addOnFailureListener { onFailure(it.message ?: "Ошибка обновления имени") }
        }
        onSuccess()
    }
}
