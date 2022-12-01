package com.brownik.simplegame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class RemainTimeViewModel : ViewModel() {

    private var _remainTime = MutableLiveData<Int>()
    val remainTime: LiveData<Int> = _remainTime

    val timerChannel = Channel<Boolean>()
    private lateinit var timerTask: Job
    private val baseGameTime = 5

    fun initRemainTime() {
        _remainTime.value = baseGameTime
    }

    private fun setRemainTime(second: Int) {
        _remainTime.postValue(second)
    }

    fun startTimer() {
        timerTask = viewModelScope.launch {
            var currentRemainTime = baseGameTime
            while(true) {
                setRemainTime(currentRemainTime)
                currentRemainTime--
                if (currentRemainTime < 0) {
                    timerChannel.send(true)
                    setRemainTime(baseGameTime)
                    stopTimer()
                }
                delay(1000L)
            }
        }
    }

    fun stopTimer() {
        if(::timerTask.isInitialized) timerTask.cancel()
        setRemainTime(baseGameTime)
    }
}