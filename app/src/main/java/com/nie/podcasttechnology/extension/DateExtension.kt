package com.nie.podcasttechnology.extension

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date {
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", getDefaultLocale())
    return dateFormat.parse(this) ?: Date()
}

fun Date.toDateString(pattern: String = "yyyy/MM/dd", zone: TimeZone = TimeZone.getDefault()): String {
    val format = SimpleDateFormat(pattern, getDefaultLocale())
    format.timeZone = zone
    return format.format(this)
}

fun getDefaultLocale(): Locale {
    return ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]
}