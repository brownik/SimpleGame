package com.brownik.simplegame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ButtonStateViewModel : ViewModel() {
    private var _buttonStateData = MutableLiveData<List<Int>>()
    val buttonStateData: LiveData<List<Int>> = _buttonStateData

    fun setData(data: MutableList<Int>) {
        _buttonStateData.value = data.toList()
    }

    // 버튼 상태 초기화
    fun initButtonStateData() {
        _buttonStateData.value = MutableList(9) { 0 }.toList()
    }

    fun initButtonBackground(){
        _buttonStateData.postValue(MutableList(9) { 0 }.toList())
    }

    private fun makeRandomPosition(): Int = (0..8).random()
    private fun makeRandomScore(): Int = makeRandomScoreList().random()

    // 버튼 상태 바꾸기
    fun changeImage() {
        val list = _buttonStateData.value?.toMutableList()
        val position = makeRandomPosition()
        val randomScore = makeRandomScore()
        Log.d("qwe123", "position: ${position}, randomState: $randomScore")
        list?.let {
            if (it[position] == randomScore || randomScore == 0) changeImage()
            else it[position] = randomScore
        }
        _buttonStateData.postValue(list?.toList())
    }

    fun onButtonClick(position: Int): Int {
        val score = when (_buttonStateData.value?.get(position)) {
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
        return score
    }

    private fun makeRandomScoreList(): List<Int> {
        val list = mutableListOf<Int>()
        list.addAll(MutableList(1){ -5 })
        list.addAll(MutableList(4){ -4 })
        list.addAll(MutableList(10){ -3 })
        list.addAll(MutableList(15){ -2 })
        list.addAll(MutableList(20){ -1 })
        list.addAll(MutableList(20){ 1 })
        list.addAll(MutableList(15){ 2 })
        list.addAll(MutableList(10){ 3 })
        list.addAll(MutableList(4){ 4 })
        list.addAll(MutableList(1){ 5 })
        return list.shuffled().toList()
    }
}