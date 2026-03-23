package com.example.cinesio.ui.components

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinesio.viewmodel.UserViewModel
import java.util.Locale
import java.text.SimpleDateFormat


@Composable
fun RedTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayLarge
        )
    }
}

fun formatNumber(n: Int): String {
    if (n < 1000) return n.toString()

    val units = arrayOf("k", "M", "B")
    var value = n.toDouble()
    var unitIndex = -1

    while (value >= 1000 && unitIndex < units.lastIndex) {
        value /= 1000
        unitIndex++
    }

    val formatted = if (value % 1.0 == 0.0)
        String.format("%.0f", value)
    else
        String.format("%.1f", value)

    return "$formatted${units[unitIndex]}"
}

fun formatReleaseDate(date: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val parsedDate = parser.parse(date)
        val formatter = SimpleDateFormat("d MMMM", Locale.FRANCE)
        formatter.format(parsedDate!!)
    } catch (e: Exception) {
        date
    }
}


fun logout(sharedPreferences: SharedPreferences, viewModel: UserViewModel) {
    sharedPreferences.edit().clear().apply()
    viewModel.logout()
}