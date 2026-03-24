package com.example.flixyConnect.data.local

import java.util.Calendar

fun canRefresh(lastUpdate: Long?): Boolean {
    val now = Calendar.getInstance()
    val last = Calendar.getInstance().apply { timeInMillis = lastUpdate ?: 0L }

    val currentWeek = now.get(Calendar.WEEK_OF_YEAR)
    val lastWeek = last.get(Calendar.WEEK_OF_YEAR)

    // si dernière update < cette semaine = refresh
    return lastWeek < currentWeek
}