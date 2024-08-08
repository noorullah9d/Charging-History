package com.example.charginghistory.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDuration(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / 1000) / 60 % 60
    val hours = (this / 1000) / 3600

    return when {
        hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }
}


fun Long.formatDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMM d, yyyy, h:mm a", Locale.getDefault())
    return format.format(date)
}