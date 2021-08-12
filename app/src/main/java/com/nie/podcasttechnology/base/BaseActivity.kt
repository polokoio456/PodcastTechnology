package com.nie.podcasttechnology.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.nie.podcasttechnology.R
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val viewModel: BaseViewModel?

    protected val compositeDisposable = CompositeDisposable()

    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(LayoutInflater.from(this).inflate(R.layout.dialog_progress, null))
            .create()

        observableLiveData()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun observableLiveData() {
        viewModel?.showLoading?.observe(this, {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun showLoading() {
        loadingDialog.show()
    }

    private fun hideLoading() {
        loadingDialog.dismiss()
    }
}