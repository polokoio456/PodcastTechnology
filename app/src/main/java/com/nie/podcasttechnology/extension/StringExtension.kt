package com.nie.podcasttechnology.extension

fun Int.toFormatTimeStr(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}