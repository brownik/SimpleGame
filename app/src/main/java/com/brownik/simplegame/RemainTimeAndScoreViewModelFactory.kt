package com.brownik.simplegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RemainTimeAndScoreViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RemainTimeAndScoreViewModel::class.java)) {
            RemainTimeAndScoreViewModel() as T
        } else {
            throw IllegalArgumentException()
        }
    }
}