package com.nie.podcasttechnology.repository

class StringRepositoryImpl : StringRepository {

    override fun getFormatTimeStr(maxDuration: Int): String {
        val hours = maxDuration / 3600
        val minutes = (maxDuration % 3600) / 60
        val seconds = maxDuration % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}