package com.brownik.simplegame.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ButtonScoreViewModel : ViewModel() {
    private var _buttonScoreData = MutableLiveData<List<Int>>()
    val buttonScoreData: LiveData<List<Int>> = _buttonScoreData



    // 버튼 상태 초기화
    fun initButtonScoreData() {
        _buttonScoreData.value = MutableList(9) { 0 }.toList()
    }

    // 백그라운드에서 버튼 상태 초기화
    fun initButtonScoreBackground() {
        _buttonScoreData.postValue(MutableList(9) { 0 }.toList())
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
            if ((it[position] > 0 && randomScore > 0) || it[position] < 0 && randomScore < 0) changeButtonScore()
            if (it[position] == randomScore) changeButtonScore()
            else it[position] = randomScore
        }
        _buttonScoreData.postValue(list?.toList())
    }

    // 버튼 클릭 시 점수 반환
    fun onButtonClick(position: Int): Int {
        val score = when (_buttonScoreData.value?.get(position)) {
            -5 -> -5
            -4 -> -4
            -3 -> -3
            -2 -> -2
            -1 -> -1
            1 -> +1
            2 -> +2
            3 -> +3
            4 -> +4
            5 -> +5
            else -> 0
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
}