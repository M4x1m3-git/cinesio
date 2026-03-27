package com.example.flixyConnect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.flixyConnect.data.store.ThemeDataStore
import com.example.flixyConnect.ui.components.AppNavigation
import com.example.flixyConnect.ui.theme.FlixyConnectTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        createChannels()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            val darkTheme by ThemeDataStore.getTheme(this).collectAsState(initial = false)

            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme

            FlixyConnectTheme(darkTheme = darkTheme) {
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



    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val releases = NotificationChannel(
                "releases_channel",
                "Sorties de films",
                NotificationManager.IMPORTANCE_DEFAULT
            )

//        val comments = NotificationChannel(
//            "comments_channel",
//            "Commentaires",
//            NotificationManager.IMPORTANCE_DEFAULT
//        )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(releases)
//        manager.createNotificationChannel(example)
        }
    }

}
