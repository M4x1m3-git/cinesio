package com.example.flixyConnect.data.repository

import android.content.Context

class NotificationRepository(context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun isEnabled(key: String): Boolean {
        return prefs.getBoolean(key, true)
    }

    fun setEnabled(key: String, enabled: Boolean) {
        prefs.edit().putBoolean(key, enabled).apply()
    }
}