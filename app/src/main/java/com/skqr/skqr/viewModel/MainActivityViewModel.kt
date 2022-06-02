package com.skqr.skqr.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivityViewModel (application: Application) : AndroidViewModel(application) {
}

class MainActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}