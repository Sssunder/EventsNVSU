package com.example.eventsnvsu.ui.theme.screens

import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventsnvsu.model.Event
import com.example.eventsnvsu.model.User
import com.example.eventsnvsu.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun EventDetailScreen(
    navController: NavController,
    event: Event,
    isOrganizer: Boolean = false,
    isAdmin: Boolean = false,
    onEdit: (() -> Unit)? = null,
    onRegister: (() -> Unit)? = null,
    onFollow: ((Boolean) -> Unit)? = null,
    eventViewModel: EventViewModel? = null // пр��брасываем для участников
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    var isFollowing by remember { mutableStateOf(event.followers.contains(currentUserId) == true) }
    val context = LocalContext.current
    val followers = event.followers
    val usersList = remember { mutableStateListOf<User>() }
    val loadedFollowers = remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val isCurrentUserOrganizer = event.organizerId == currentUserId

    LaunchedEffect(event.id, event.followers, isAdmin, isCurrentUserOrganizer) {
        val followersSet = followers.toMutableSet()
        if ((isOrganizer || isAdmin) && currentUser != null) {
            // Гарантируем, что организатор или админ всегда в списке участников
            followersSet.add(currentUser.uid)
        }
        if ((isOrganizer || isAdmin) && followersSet.isNotEmpty()) {
            com.example.eventsnvsu.data.FirebaseRepository().getUsersByIds(
                followersSet.toList(),
                onSuccess = { users ->
                    usersList.clear()
                    usersList.addAll(users)
                },
                onFailure = { /* ignore for now */ }
            )
        } else if ((isOrganizer || isAdmin) && followersSet.isEmpty() && currentUser != null) {
            usersList.clear()
            usersList.add(
                com.example.eventsnvsu.model.User(
                    id = currentUser.uid,
                    email = currentUser.email ?: "",
                    name = currentUser.displayName ?: currentUser.email ?: "",
                    role = "user"
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)), // прозрачный тёмный фон
        contentAlignment = Alignment.Center
    ) {
        // Убрали градиентный фон
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.92f, animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 0.92f, animationSpec = tween(200))
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(min = 320.dp, max = (0.7f * LocalContext.current.resources.displayMetrics.heightPixels / LocalContext.current.resources.displayMetrics.density).dp)
                    .padding(16.dp)
            ) {
                androidx.compose.foundation.rememberScrollState().let { scrollState ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            // Bookmark чекбокс в правом верхнем углу
                            val bookmarkChecked = remember { mutableStateOf(event.followers.contains(currentUserId)) }
                            val bookmarkColor by animateColorAsState(
                                if (bookmarkChecked.value) MaterialTheme.colorScheme.primary else Color.Gray,
                                label = "bookmarkColor"
                            )
                            val bookmarkAlpha by animateFloatAsState(
                                if (bookmarkChecked.value) 1f else 0.7f,
                                label = "bookmarkAlpha"
                            )
                            IconButton(
                                onClick = {
                                    if (!bookmarkChecked.value) {
                                        onRegister?.invoke()
                                        bookmarkChecked.value = true
                                        onFollow?.invoke(true)
                                    } else {
                                        // Отписка
                                        bookmarkChecked.value = false
                                        onFollow?.invoke(false)
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = if (bookmarkChecked.value) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = if (bookmarkChecked.value) "Вы записаны" else "Записаться",
                                    tint = bookmarkColor,
                                    modifier = Modifier.size(32.dp).then(Modifier)
                                )
                            }
                            Column(
                                modifier = Modifier.padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(event.title, style = MaterialTheme.typography.headlineLarge)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF165DAC), modifier = Modifier.padding(end = 4.dp))
                                    Text(event.date, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF165DAC), modifier = Modifier.padding(end = 4.dp))
                                    Text(event.location, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                }
                                if (event.tags.isNotEmpty()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Tag, contentDescription = null, tint = Color(0xFF36D1DC), modifier = Modifier.padding(end = 4.dp))
                                        Text(event.tags.joinToString(), style = MaterialTheme.typography.labelMedium, color = Color(0xFF36D1DC))
                                    }
                                }
                                Text(event.description, style = MaterialTheme.typography.bodyLarge)
                                Spacer(Modifier.height(16.dp))
                                if ((isCurrentUserOrganizer || isAdmin) && onEdit != null) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        // Кнопка редактирования с заливкой
                                        IconButton(
                                            onClick = { onEdit() },
                                            modifier = Modifier
                                                .size(44.dp)
                                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Редактировать",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Spacer(Modifier.width(12.dp))
                                        // Кнопка удаления с заливкой
                                        IconButton(
                                            onClick = { showDeleteDialog.value = true },
                                            modifier = Modifier
                                                .size(44.dp)
                                                .background(Color(0xFFE53935), RoundedCornerShape(50))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Удалить",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                    if (showDeleteDialog.value) {
                                        AlertDialog(
                                            onDismissRequest = { showDeleteDialog.value = false },
                                            title = { Text("Удалить мероприятие?") },
                                            text = { Text("Вы уверены, что хотите удалить это мероприятие? Это действие необратимо.") },
                                            confirmButton = {
                                                TextButton(onClick = {
                                                    showDeleteDialog.value = false
                                                    coroutineScope.launch {
                                                        eventViewModel?.deleteEvent(
                                                            event.id,
                                                            onSuccess = {
                                                                android.widget.Toast.makeText(context, "Мероприятие удалено", android.widget.Toast.LENGTH_SHORT).show()
                                                                navController.popBackStack()
                                                            },
                                                            onFailure = { err ->
                                                                android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_SHORT).show()
                                                            }
                                                        )
                                                    }
                                                }) {
                                                    Text("Удалить", color = Color.Red)
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { showDeleteDialog.value = false }) {
                                                    Text("Отмена")
                                                }
                                            }
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    // Список подписчиков
                                    Text("Подписчики:", style = MaterialTheme.typography.titleMedium)
                                    if (usersList.isEmpty()) {
                                        Text("Нет подписчиков", color = Color.Gray, modifier = Modifier.padding(8.dp))
                                    } else {
                                        LazyColumn(modifier = Modifier.heightIn(max = 180.dp)) {
                                            items(usersList.sortedBy { it.name.ifBlank { it.email } }) { user ->
                                                Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp),
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(Modifier.width(8.dp))
                                                    Text(user.name.ifBlank { user.email }, style = MaterialTheme.typography.bodyMedium)
                                                }
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick = {
                                        // Экспорт в CSV в папку загрузки
                                        val csv = StringBuilder()
                                        csv.append("ФИО,Email\n")
                                        for (user in usersList) {
                                            csv.append("\"${user.name}\",\"${user.email}\"\n")
                                        }
                                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        val file = File(downloadsDir, "participants_${event.id}.csv")
                                        try {
                                            OutputStreamWriter(FileOutputStream(file)).use { it.write(csv.toString()) }
                                            android.widget.Toast.makeText(context, "Файл сохранён: ${file.absolutePath}", android.widget.Toast.LENGTH_LONG).show()
                                        } catch (e: Exception) {
                                            android.widget.Toast.makeText(context, "Ошибка сохранения файла: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                                        }
                                    }, modifier = Modifier.fillMaxWidth()) {
                                        Text("Сохранить участников (CSV)")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
