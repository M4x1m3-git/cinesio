package com.example.cinesio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cinesio.ui.components.ThemeSwitcher

@Composable
fun ProfilScreen(navController: NavController, darkTheme: Boolean, onThemeUpdated: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Button(
            onClick = { navController.navigate("login") }
        ) {
            Text("Connexion")
        }

        ThemeSwitcher(
            darkTheme = darkTheme,
            onClick = onThemeUpdated
        )

    }
}