package com.example.eventsnvsu.data

import android.net.Uri
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    // Добавляем публичное свойство для доступа к текущему пользователю
    val currentUser: FirebaseUser?
        get() = auth.currentUser

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

    fun register(
        email: String,
        password: String,
        name: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf(
                        "role" to role,
                        "name" to name,
                        "email" to email,
                        "id" to (user?.uid ?: "")
                    )
                    db.collection("users").document(user?.uid ?: "").set(userData)
                        .addOnSuccessListener {
                            user?.sendEmailVerification()
                                ?.addOnSuccessListener { onSuccess() }
                                ?.addOnFailureListener { e -> onFailure("Ошибка отправки письма: ${e.message}") }
                        }
                        .addOnFailureListener { e -> onFailure(e.message ?: "Unknown error") }
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
            .addOnFailureListener { e -> // Исправлено на addOnFailureListener
                onFailure(e.message ?: "Unknown error") // Исправлено на e.message
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
        val docRef = db.collection("events").document()
        val eventWithId = event.copy(id = docRef.id)
        docRef.set(eventWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка создания мероприятия")
            }
    }

    fun updateEvent(event: Event, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (event.id.isEmpty()) {
            onFailure("Некорректный id мероприятия")
            return
        }
        db.collection("events").document(event.id).set(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка обновления мероприятия")
            }
    }

    fun getOrganizerEvents(
        organizerId: String,
        onSuccess: (List<Event>) -> Unit,
        onFailure: (String) -> Unit
    ) {
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
        db.collection("events").document(eventId)
            .update(
                "followers", FieldValue.arrayUnion(user?.uid)
            )
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
        val updates = hashMapOf<String, Any>()
        if (!newEmail.isNullOrBlank() && newEmail != user.email) {
            user.updateEmail(newEmail)
                .addOnFailureListener { onFailure(it.message ?: "Ошибка обновления email") }
            updates["email"] = newEmail
        }
        if (!newPassword.isNullOrBlank()) {
            user.updatePassword(newPassword)
                .addOnFailureListener { onFailure(it.message ?: "Ошибка обновления пароля") }
        }
        if (!newName.isNullOrBlank()) {
            updates["name"] = newName
        }
        if (updates.isNotEmpty()) {
            db.collection("users").document(user.uid).update(updates as Map<String, Any>)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it.message ?: "Ошибка обновления профиля") }
        } else {
            onSuccess()
        }
    }

    fun signOut() {
        auth.signOut()
    }

    // --- Подписка на мероприятие ---
    fun followEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onFailure("Пользователь не найден")
            return
        }
        db.collection("events").document(eventId)
            .update("followers", FieldValue.arrayUnion(user.uid))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Ошибка подписки") }
    }

    fun unfollowEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onFailure("Пользователь не найден")
            return
        }
        db.collection("events").document(eventId)
            .update("followers", FieldValue.arrayRemove(user.uid))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Ошибка отписки") }
    }

    fun uploadProfilePhoto(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser ?: return onFailure("Пользователь не найден")
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_photos/${user.uid}.jpg")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Сохраняем ссылку в Firestore
                    db.collection("users").document(user.uid)
                        .update("photoUrl", downloadUri.toString())
                        .addOnSuccessListener { onSuccess(downloadUri.toString()) }
                        .addOnFailureListener { e -> onFailure(e.message ?: "Ошибка обновления photoUrl") }
                }.addOnFailureListener { e -> onFailure(e.message ?: "Ошибка получения ссылки") }
            }
            .addOnFailureListener { e -> onFailure(e.message ?: "Ошибка загрузки фото") }
    }

    fun getUsersByIds(
        userIds: List<String>,
        onSuccess: (List<User>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (userIds.isEmpty()) {
            onSuccess(emptyList())
            return
        }
        db.collection("users")
            .whereIn("id", userIds)
            .get()
            .addOnSuccessListener { result ->
                val users = result.mapNotNull { it.toObject(User::class.java) }
                onSuccess(users)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка загрузки пользователей")
            }
    }

    fun deleteEvent(eventId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("events").document(eventId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Ошибка удаления мероприятия") }
    }
}
