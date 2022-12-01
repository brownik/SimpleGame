package com.brownik.simplegame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    fun initScore() = run { _score.value = 0 }

    fun calculationScore(score: Int) {
        val currentScore = _score.value
        _score.value = currentScore?.plus(score)
    }
}