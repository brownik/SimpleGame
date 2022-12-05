package com.brownik.simplegame.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brownik.simplegame.MyObject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class RemainTimeViewModel : ViewModel() {

    private val baseGameTime = 5

    private var _timerState = MutableStateFlow<TimerState>(TimerState.End)
    val timerState = _timerState

    private var _remainTime = MutableLiveData(baseGameTime)
    val remainTime: LiveData<Int> = _remainTime

    val timerChannel = Channel<Boolean>()
    private lateinit var timerTask: Job
    private lateinit var timerFlow: Flow<Int>

    private var isGaming = false

    // 타이머 초기화
    fun initTimer() {
        _remainTime.postValue(baseGameTime)
    }

    fun startTimer() {
        (baseGameTime downTo 0)
            .asFlow()
            .takeWhile { isGaming }
            .onStart { _timerState.emit(TimerState.Start) }
            .onEach {
                setRemainTime(it)
                delay(1000L)
            }.onCompletion { _timerState.emit(TimerState.End) }
            .launchIn(viewModelScope)
    }

    fun changeGameState(isStartGame: Boolean) {
        isGaming = isStartGame
    }

    // remainTime 뷰를 업데이트 해주는 함수
    private fun setRemainTime(second: Int) {
        _remainTime.value = second
    }

    // 타이머를 시작하는 함수
    // 남은 시간이 0이 되면 timeChannel에 Boolean 값을 send
    fun startTimerUseChannel() {
        timerTask = viewModelScope.launch {
            var currentRemainTime = baseGameTime
            while (true) {
                setRemainTime(currentRemainTime)
                currentRemainTime--
                if (currentRemainTime < 0) {
                    timerChannel.send(true)
                    setRemainTime(baseGameTime)
                    stopTimerUseChannel()
                }
                delay(1000L)
            }
        }
    }

    // 타이머를 멈추는 함수
    fun stopTimerUseChannel() {
        if (::timerTask.isInitialized) timerTask.cancel()
        setRemainTime(baseGameTime)
    }

    private fun startTimerUseFlow(): Flow<Int> {
        timerFlow = flow {
            var currentRemainTime = baseGameTime
            while(currentRemainTime > 0) {
                emit(currentRemainTime)
                currentRemainTime--
                delay(1000L)
            }
            emit(baseGameTime)
        }
        return timerFlow
    }
}

sealed class TimerState {
    object Start: TimerState()
    object End: TimerState()
}