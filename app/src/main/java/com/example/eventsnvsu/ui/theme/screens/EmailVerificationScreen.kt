package com.example.eventsnvsu.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventsnvsu.viewmodel.AuthViewModel

@Composable
fun EmailVerificationScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val user = authViewModel.currentUser
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Подтвердите ваш email",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Письмо с подтверждением отправлено на ${user?.email ?: "вашу почту"}.\nПроверьте почту и перейдите по ссылке для активации аккаунта.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(onClick = {
            authViewModel.tryLoginAfterVerification(
                onSuccess = {
                    Toast.makeText(context, "Email подтвержден!", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("email_verification") { inclusive = true }
                    }
                },
                onFailure = { err ->
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                }
            )
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Я подтвердил email")
        }
        Button(onClick = {
            user?.sendEmailVerification()?.addOnSuccessListener {
                Toast.makeText(context, "Письмо отправлено повторно.", Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener {
                Toast.makeText(context, "Ошибка отправки письма.", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Отправить письмо еще раз")
        }
    }
}
