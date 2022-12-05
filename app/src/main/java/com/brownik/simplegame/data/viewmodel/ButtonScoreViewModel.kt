package com.brownik.simplegame.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brownik.simplegame.MyObject
import com.brownik.simplegame.data.model.ButtonDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ButtonScoreViewModel : ViewModel() {
    private var _buttonScoreData = MutableLiveData<List<Int>>()
    val buttonScoreData: LiveData<List<Int>> = _buttonScoreData

    private var isGaming = false

    init {
        initButtonScoreData()
    }

    // 버튼 상태 초기화
    private fun initButtonScoreData() {
        _buttonScoreData.value = MutableList(9) { 0 }.toList()
    }

    // 버튼 상태 초기화(메인 쓰레드 X)
    private fun initButtonScoreBackground() {
        _buttonScoreData.postValue(MutableList(9) { 0 }.toList())
    }

    fun changeGameState(isStartGame: Boolean) {
        isGaming = isStartGame
    }

    fun startGame() {
        changeButtonScoreUserFlow()
            .onStart {
                MyObject.makeLog("startGame.game flow onStart")
            }.onEach { buttonData ->
                val list = _buttonScoreData.value?.toMutableList()
                list?.let { list ->
                    list[buttonData.position] = buttonData.score
                    _buttonScoreData.postValue(list.toList())
                }
            }.onCompletion {
                MyObject.makeLog("startGame.game flow onCompletion")
            }.launchIn(viewModelScope)
    }

    fun stopGame() {
        initButtonScoreBackground()
    }

    private fun makeRandomPosition(): Int = (0..8).random()

    private fun makeRandomScore(): Int {
        var score = 0
        while (score == 0) score = makeRandomScoreList().random()
        return score
    }

    // 버튼 상태 바꾸기
    fun changeButtonScore() {
        val list = _buttonScoreData.value?.toMutableList()
        val position = makeRandomPosition()
        val randomScore = makeRandomScore()
        list?.let {
            if ((it[position] > 0 && randomScore > 0) || (it[position] < 0 && randomScore < 0)) changeButtonScore()
            if (it[position] == randomScore) changeButtonScore()
            else it[position] = randomScore
        }
        _buttonScoreData.postValue(list?.toList())
    }

    private fun changeButtonScoreUserFlow(): Flow<ButtonDataModel> = flow {
        while (isGaming) {
            val list = _buttonScoreData.value?.toMutableList()
            val position = makeRandomPosition()
            val randomScore = makeRandomScore()
            if (list?.get(position) == randomScore) continue
            else emit(ButtonDataModel(position, randomScore))
            delay(makeRandomTime())
        }
    }

    // 버튼 클릭 시 점수 반환
    fun onButtonClick(position: Int): Int {
        var score = 0
        _buttonScoreData.value?.let {
            score = when (_buttonScoreData.value?.get(position)) {
                0 -> 0
                else -> _buttonScoreData.value!![position]
            }
        }
        initOneButton(position)
        return score
    }

    // 버튼 초기화 함수
    private fun initOneButton(position: Int) {
        val list = _buttonScoreData.value?.toMutableList()
        list?.let {
            it[position] = 0
        }
        _buttonScoreData.postValue(list?.toList())
    }

    // 점수 확률을 나눈 리스트
    private fun makeRandomScoreList(): List<Int> {
        val list = mutableListOf<Int>()
        list.addAll(MutableList(1) { -5 })
        list.addAll(MutableList(4) { -4 })
        list.addAll(MutableList(10) { -3 })
        list.addAll(MutableList(15) { -2 })
        list.addAll(MutableList(20) { -1 })
        list.addAll(MutableList(20) { 1 })
        list.addAll(MutableList(15) { 2 })
        list.addAll(MutableList(10) { 3 })
        list.addAll(MutableList(4) { 4 })
        list.addAll(MutableList(1) { 5 })
        return list.shuffled().toList()
    }

    // 버튼을 바꾸기 위한 시간을 랜덤으로 정하는 함수
    private fun makeRandomTime(): Long = (0..500).random().toLong()
}