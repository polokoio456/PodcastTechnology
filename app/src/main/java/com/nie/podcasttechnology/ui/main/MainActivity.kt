package com.nie.podcasttechnology.ui.main

import android.os.Bundle
import com.nie.podcasttechnology.base.BaseActivity
import com.nie.podcasttechnology.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.fetchPodcasts()

        observableLiveData()
    }

    private fun observableLiveData() {
        viewModel.rss.observe(this, {

        })
    }
}