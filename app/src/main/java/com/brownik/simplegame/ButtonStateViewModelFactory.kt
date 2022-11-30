package com.brownik.simplegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ButtonStateViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ButtonStateViewModel::class.java)) {
            ButtonStateViewModel() as T
        } else {
            throw IllegalArgumentException()
        }
    }
}