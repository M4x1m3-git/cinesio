package com.example.flixyConnect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(
    error: String,
    onRetry: () -> Unit
) {

    val icon: ImageVector
    val title: String
    val description: String

    if (error == "Erreur réseau") {

        icon = Icons.Default.WifiOff
        title = "Pas de connexion"
        description = "Vérifiez votre connexion internet"

    } else if (error == "Erreur inattendue") {

        icon = Icons.Default.Warning
        title = "Oups..."
        description = "Une erreur inattendue est survenue"

    } else {

        icon = Icons.Default.Error
        title = "Erreur"
        description = "Impossible de charger les données"

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Réessayer")
        }
    }
}