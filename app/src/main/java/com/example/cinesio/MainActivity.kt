package com.example.cinesio

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cinesio.ui.screens.MainScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.cinesio.data.model.Movie
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cinesio.data.store.ThemeDataStore
import com.example.cinesio.ui.components.AppNavigation
import com.example.cinesio.ui.theme.CinesioTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            val darkTheme by ThemeDataStore.getTheme(this).collectAsState(initial = false)

            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme

            CinesioTheme(darkTheme = darkTheme) {
                AppNavigation(
                    darkTheme = darkTheme,
                    onThemeUpdated = {
                        lifecycleScope.launch {
                            ThemeDataStore.saveTheme(this@MainActivity, !darkTheme)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun elMetierText(film: Movie, onClick: (Movie) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
            .clickable {
                onClick(film)
            }
    ) {
        Text(
            text = film.title,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp)
        )
    }

}

@Composable
fun listElMetier(
    films: List<Movie>,
    onItemClick: (Movie) -> Unit
) {
    Column (
        modifier = Modifier
            .padding(16.dp)
    ) {
        if (films.isEmpty()) {
            Text("Aucun film trouvé")
        } else {
            for (film in films) {
                elMetierText(film) {
                    onItemClick(it)
                }
            }
        }
    }
}
