package com.nie.podcasttechnology.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    protected fun showLoading() {
        _showLoading.postValue(true)
    }

    protected fun hideLoading() {
        _showLoading.postValue(false)
    }
}