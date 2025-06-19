package com.example.eventsnvsu.ui.theme.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eventsnvsu.navigation.Screen
import com.example.eventsnvsu.navigation.getIconForRoute
import com.example.eventsnvsu.navigation.getLabelForRoute
import com.example.eventsnvsu.ui.theme.EventsNVSUTheme
import com.example.eventsnvsu.viewmodel.AuthViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    initialIsOrganizer: Boolean = true, // временно, на случай если роль не загрузится
    contentPadding: PaddingValues = PaddingValues()
) {
    val isOrganizer = authViewModel.currentUserRole.value == "organizer"
    val isLoggedIn = authViewModel.currentUser != null

    // Если не залогинен — сразу на экран логина
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(com.example.eventsnvsu.navigation.Screen.Login.route) {
                popUpTo(com.example.eventsnvsu.navigation.Screen.Main.route) { inclusive = true }
            }
        }
    }

    // Градиентный фон с блюром (уникальный для главного экрана)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.matchParentSize().blur(24.dp)) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF165DAC), Color(0xFF36D1DC), Color(0xFFB2FEFA)),
                    start = Offset(0f, size.height * 0.2f),
                    end = Offset(size.width, size.height)
                ),
                size = size
            )
        }
        // Основной контент — большие карточки мероприятий на свайпе
        EventListScreen(navController = navController)
    }
}

@Composable
fun AppleStyleBottomBar(
    navController: NavHostController,
    screens: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(72.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val selected = currentRoute == screen.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = getIconForRoute(screen.route),
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            text = getLabelForRoute(screen.route),
                            fontSize = 12.sp,
                            color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(screen.route)
                        }
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    EventsNVSUTheme {
        val navController = rememberNavController()
        // Для превью используе�� мок-объект или заменитель ViewModel
        MainScreen(navController, authViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), initialIsOrganizer = true)
    }
}
