//// Добавляем недостающие импорты
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.QuerySnapshot
//import com.example.eventsnvsu.model.Event
//import com.example.eventsnvsu.model.User
//import kotlin.jvm.java
//
//class FirebaseRepository {
//    private val auth = FirebaseAuth.getInstance()
//    private val db = FirebaseFirestore.getInstance()
//    private val usersRef = db.collection("users")
//    private val eventsRef = db.collection("events")
//
//    fun register(
//        email: String,
//        password: String,
//        role: String,
//        onSuccess: () -> Unit,
//        onFailure: (String) -> Unit
//    ) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnSuccessListener { authResult ->
//                val user = hashMapOf(
//                    "email" to email,
//                    "role" to role
//                )
//                usersRef.document(authResult.user?.uid ?: "")
//                    .set(user)
//                    .addOnSuccessListener { onSuccess() }
//                    .addOnFailureListener { e ->
//                        onFailure(e.message ?: "Ошибка регистрации")
//                    }
//            }
//            .addOnFailureListener { e ->
//                onFailure(e.message ?: "Ошибка регистрации")
//            }
//    }
//
//    fun getUserRole(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
//        val userId = auth.currentUser?.uid ?: run {
//            onFailure("Пользователь не авторизован")
//            return
//        }
//
//        usersRef.document(userId).get()
//            .addOnSuccessListener { document ->
//                val role = document.getString("role") ?: "user"
//                onSuccess(role)
//            }
//            .addOnFailureListener { e ->
//                onFailure(e.message ?: "Ошибка получения данных")
//            }
//    }
//
//    fun createEvent(event: Event, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
//        eventsRef.add(event)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { e ->
//                onFailure(e.message ?: "Ошибка создания мероприятия")
//            }
//    }
//
//    fun getEvents(onSuccess: (List<Event>) -> Unit, onFailure: (String) -> Unit) {
//        eventsRef.get()
//            .addOnSuccessListener { querySnapshot ->
//                val events = querySnapshot.toObjects(Event::class.java)
//                onSuccess(events)
//            }
//            .addOnFailureListener { e ->
//                onFailure(e.message ?: "Ошибка загрузки мероприятий")
//            }
//    }
//
//    fun getUserProfile(userId: String, onResult: (User?) -> Unit) {
//        usersRef.document(userId).get()
//            .addOnSuccessListener { document ->
//                val user = document.toObject(User::class.java)
//                onResult(user)
//            }
//    }
//}




// глушилка
package com.example.eventsnvsu.data

import com.example.eventsnvsu.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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
    // Эмуляция получения данных
    fun getEvents(onSuccess: (List<Event>) -> Unit, onFailure: (String) -> Unit) {
        // Тестовые данные вместо Firebase
        val events = listOf(
            Event("Событие 1", "2025-04-12"),
            Event("Событие 2", "2025-04-13"),
            Event("Событие 3", "2025-04-14")
        )

        // Симуляция успешного вызова
        onSuccess(events)
    }

    fun createEvent(event: com.example.eventsnvsu.model.Event, onSuccess: () -> kotlin.Boolean, onFailure: Any) {}
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
}
