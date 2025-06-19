package com.example.eventsnvsu.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eventsnvsu.viewmodel.AuthViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainNavigationScaffold(
    navController: NavHostController,
    isOrganizer: Boolean,
    authViewModel: AuthViewModel
) {
    val items = getBottomNavItems(isOrganizer)
    val bottomNavController = androidx.navigation.compose.rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in items.map { it.screen.route }) {
                // Овальное меню
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        tonalElevation = 8.dp,
                        shadowElevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(72.dp)
                    ) {
                        NavigationBar {
                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.screen.route,
                                    onClick = {
                                        bottomNavController.navigate(item.screen.route) {
                                            popUpTo(items.first().screen.route) { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title) }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        BottomNavGraph(
            navController = bottomNavController,
            rootNavController = navController,
            isOrganizer = isOrganizer,
            authViewModel = authViewModel
        )
    }
}
