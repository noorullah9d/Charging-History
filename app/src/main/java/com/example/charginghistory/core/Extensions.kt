package com.example.charginghistory.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("MMM d, yyyy, h:mm a", Locale.getDefault())
    return format.format(date)
}