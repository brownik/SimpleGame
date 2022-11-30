package com.brownik.simplegame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemainTimeAndScoreViewModel: ViewModel() {

    private var _remainTime = MutableLiveData<Int>()
    val remainTime: LiveData<Int> = _remainTime

    fun setRemainTime(second: Int) {
        _remainTime.postValue(second)
    }
}