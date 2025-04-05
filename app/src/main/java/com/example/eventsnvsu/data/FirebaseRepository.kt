// Добавляем недостающие импорты
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.model.User
import kotlin.jvm.java

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersRef = db.collection("users")
    private val eventsRef = db.collection("events")

    fun register(
        email: String,
        password: String,
        role: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = hashMapOf(
                    "email" to email,
                    "role" to role
                )
                usersRef.document(authResult.user?.uid ?: "")
                    .set(user)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e ->
                        onFailure(e.message ?: "Ошибка регистрации")
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка регистрации")
            }
    }

    fun getUserRole(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure("Пользователь не авторизован")
            return
        }

        usersRef.document(userId).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role") ?: "user"
                onSuccess(role)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка получения данных")
            }
    }

    fun createEvent(event: Event, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        eventsRef.add(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка создания мероприятия")
            }
    }

    fun getEvents(onSuccess: (List<Event>) -> Unit, onFailure: (String) -> Unit) {
        eventsRef.get()
            .addOnSuccessListener { querySnapshot ->
                val events = querySnapshot.toObjects(Event::class.java)
                onSuccess(events)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Ошибка загрузки мероприятий")
            }
    }

    fun getUserProfile(userId: String, onResult: (User?) -> Unit) {
        usersRef.document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                onResult(user)
            }
    }
}