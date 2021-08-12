package com.nie.podcasttechnology.ui.audioplay

import com.nie.podcasttechnology.base.BaseViewModel
import com.nie.podcasttechnology.repository.StringRepository

class AudioPlayViewModel(private val stringRepository: StringRepository) : BaseViewModel() {

    fun getFormatTimeStr(maxDuration: Int) = stringRepository.getFormatTimeStr(maxDuration)
}