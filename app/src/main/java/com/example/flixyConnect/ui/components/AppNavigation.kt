package com.example.flixyConnect.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flixyConnect.data.store.ThemeDataStore
import com.example.flixyConnect.ui.screens.MainScreen
import kotlinx.coroutines.delay
import com.example.flixyConnect.R

@Composable
fun AppNavigation(darkTheme: Boolean, onThemeUpdated: () -> Unit) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("main") {
            val context = LocalContext.current
            MainScreen(
                darkTheme = darkTheme,
                onThemeUpdated = onThemeUpdated,
                context = context
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {

    val rotation = remember { Animatable(-40f) }

    LaunchedEffect(Unit) {

        rotation.animateTo(
            targetValue = 0f,
            animationSpec = tween(800)
        )

        delay(300)

        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(R.drawable.clap_red),
            contentDescription = "Clap",
            modifier = Modifier
                .size(150.dp)
                .rotate(rotation.value)
        )
    }
}