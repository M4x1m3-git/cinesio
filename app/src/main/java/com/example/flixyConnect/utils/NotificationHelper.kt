package com.example.flixyConnect.utils

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.Context


object NotificationHelper {

    fun sendReleaseNotification(context: Context, title: String) {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val enabled = prefs.getBoolean(NotificationPreferences.RELEASES, true)
        if (!enabled) return

        val notification = NotificationCompat.Builder(context, "releases_channel")
            .setContentTitle("Nouveau film disponible 🎬")
            .setContentText(title)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

}