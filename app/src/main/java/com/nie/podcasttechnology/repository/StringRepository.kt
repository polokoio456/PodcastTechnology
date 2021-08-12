package com.nie.podcasttechnology.repository

interface StringRepository {
    fun getFormatTimeStr(maxDuration: Int): String
}