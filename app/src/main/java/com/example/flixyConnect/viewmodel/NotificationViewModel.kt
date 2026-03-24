package com.example.flixyConnect.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.flixyConnect.data.repository.NotificationRepository
import com.example.flixyConnect.utils.NotificationPreferences
import com.example.flixyConnect.utils.hasNotificationPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = NotificationRepository(application)

    private val _releaseEnabled = MutableStateFlow(true)
    val releaseEnabled: StateFlow<Boolean> = _releaseEnabled

//    private val _exampleEnabled = MutableStateFlow(true)
//    val exampleEnabled: StateFlow<Boolean> = _exampleEnabled


    fun load(context: Context) {
        val hasPermission = hasNotificationPermission(context)

        val release = repository.isEnabled(NotificationPreferences.RELEASES)

        _releaseEnabled.value = if (hasPermission) release else false
    }

    fun toggleRelease(enabled: Boolean) {
        repository.setEnabled(NotificationPreferences.RELEASES, enabled)
        _releaseEnabled.value = enabled
    }

//    fun toggleExample(enabled: Boolean) {
//        repository.setEnabled(NotificationPreferences.COMMENTS, enabled)
//        _exampleEnabled.value = enabled
//    }
}