package com.brownik.simplegame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ButtonStateViewModel : ViewModel() {
    private var _buttonStateData = MutableLiveData<List<ButtonStateData>>()
    val buttonStateData: LiveData<List<ButtonStateData>> = _buttonStateData

    fun setData(data: MutableList<ButtonStateData>) {
        _buttonStateData.value = data.toList()
        Log.d("qwe123", "setData: ${data[0].buttonState}")
    }

    // 버튼 상태 초기화
    fun initButtonStateData() {
        _buttonStateData.value = mutableListOf(
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
            ButtonStateData(),
        ).toList()
    }

    private fun makeRandomPosition(): Int = (0..8).random()
    private fun makeRandomState(): Int = (0..1).random()

    // 버튼 상태 바꾸기
    suspend fun changeImage() {
        val list = _buttonStateData.value
        val position = makeRandomPosition()
        val randomState = makeRandomState()
        Log.d("qwe123", "position: ${position}, randomState: $randomState")
        list?.let {
            if(it[position].buttonState == randomState) changeImage()
            else it[position].buttonState = randomState
        }
        _buttonStateData.postValue(list?.toList())
    }

    fun onButtonClick(position: Int): Int{
        val score = when(position){
            0 -> -1
            1 -> +1
            else -> 0
        }
        return score
    }
}