package com.example.eventsnvsu.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.eventsnvsu.model.Event

@Composable
fun EventCard(event: Event, onClick: () -> Unit, cardWidth: Dp) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .width(cardWidth)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF165DAC), Color(0xFF36D1DC)),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(800f, 800f)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Дата мероприятия
                    if (event.date.isNotBlank()) {
                        Text(
                            text = event.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    if (!event.chatLink.isNullOrBlank()) {
                        val context = LocalContext.current
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Чат",
                            tint = Color(0xFF36D1DC),
                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, event.chatLink.toUri())
                                    context.startActivity(intent)
                                }
                        )
                    }
                }
                if (event.tags.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = null,
                            tint = Color(0xFF36D1DC),
                            modifier = Modifier.size(14.dp).padding(end = 2.dp)
                        )
                        Text(event.tags.joinToString(), style = MaterialTheme.typography.labelSmall, color = Color(0xFF36D1DC))
                    }
                }
                if (event.location.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF165DAC), modifier = Modifier.size(14.dp).padding(end = 2.dp))
                        Text(event.location, style = MaterialTheme.typography.labelSmall, color = Color(0xFF165DAC))
                    }
                }
                if (event.description.isNotBlank()) {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
                // --- Показываем участников (имена или email) ---
                if (event.followers.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Участники",
                            tint = Color(0xFF165DAC),
                            modifier = Modifier.size(14.dp).padding(end = 2.dp)
                        )
                        Text("Участников: ", style = MaterialTheme.typography.labelSmall, color = Color(0xFF165DAC))
                        Text(event.followers.size.toString(), style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
fun BigEventCard(event: Event, onClick: () -> Unit, cardWidth: Dp) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .width(cardWidth)
            .heightIn(min = 260.dp, max = 340.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 6.dp),
                maxLines = 2
            )
            if (event.tags.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = null,
                        tint = Color(0xFF36D1DC),
                        modifier = Modifier.size(14.dp).padding(end = 2.dp)
                    )
                    Text(
                        event.tags.joinToString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF36D1DC)
                    )
                }
            }
            if (event.location.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF165DAC),
                        modifier = Modifier.size(14.dp).padding(end = 2.dp)
                    )
                    Text(
                        event.location,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF165DAC)
                    )
                }
            }
            if (event.date.isNotBlank()) {
                Text(
                    text = event.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            if (event.description.isNotBlank()) {
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    modifier = Modifier.padding(top = 2.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            if (event.followers.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Участники",
                        tint = Color(0xFF165DAC),
                        modifier = Modifier.size(14.dp).padding(end = 2.dp)
                    )
                    Text("Участников: ", style = MaterialTheme.typography.labelSmall, color = Color(0xFF165DAC))
                    Text(event.followers.size.toString(), style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1)
                }
            }
            if (!event.chatLink.isNullOrBlank()) {
                val context = LocalContext.current
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Чат",
                    tint = Color(0xFF36D1DC),
                    modifier = Modifier
                        .size(18.dp)
                        .padding(top = 6.dp)
                        .clickable {
                            val intent = android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                event.chatLink.toUri()
                            )
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}
