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

    fun setRandomData() {
        val testData = mutableListOf(
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
            ButtonStateData(makeRandomNum()),
        )
        _buttonStateData.value = testData.toList()
    }

    private fun makeRandomNum(): Int = (0..2).random()
}