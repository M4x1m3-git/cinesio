package com.example.cinesio.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cinesio.data.local.database.AppDatabase
import com.example.cinesio.data.repository.UserRepository
import com.example.cinesio.ui.components.ThemeSwitcher
import com.example.cinesio.ui.components.logout
import com.example.cinesio.viewmodel.UserViewModel

@Composable
fun ProfilScreen(navController: NavController, darkTheme: Boolean, onThemeUpdated: () -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    var PREFS_KEY = "prefs"

    val sharedPreferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    val savedEmail = sharedPreferences.getString("email", null)
    val savedUsername = sharedPreferences.getString("username", null)

    val currentUser by viewModel.currentUser.collectAsState()
    println(currentUser)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (savedEmail == null) {
            Button(
                onClick = { navController.navigate("login") }
            ) {
                Text("Connexion")
            }
        } else {
            Button(
                onClick = {
                    logout(sharedPreferences, viewModel)
                }
            ) {
                Text("LogOut")
            }
        }

        ThemeSwitcher(
            darkTheme = darkTheme,
            onClick = onThemeUpdated
        )
    }
}