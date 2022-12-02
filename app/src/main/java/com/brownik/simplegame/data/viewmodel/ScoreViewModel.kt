package com.brownik.simplegame.data.viewmodel

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ScoreViewModel : ViewModel() {
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    val scoreValueAnimatorChannel = Channel<ValueAnimator>()
    val scoreObjectAnimatorChannel = Channel<ObjectAnimator>()

    fun initScore() = run { _score.value = 0 }

    // 총 점수 뷰를 업데이트 해주는 함수
    fun totalScore(score: Int, view: TextView) {
        val currentScore = _score.value
        _score.value = currentScore?.plus(score)
        makeAnimator(score, view)
    }

    // 새로 들어온 점수에 따라 Animator를 구분해 channel에 쌓아주는 함수
    private fun makeAnimator(score: Int?, view: TextView) {
        viewModelScope.launch {
            score?.let {
                if (it > 0) {
                    scoreValueAnimatorChannel.send(plusAnimator(view))
                } else if (it < 0) {
                    scoreObjectAnimatorChannel.send(minusAnimator(view))
                }
            }
        }
    }

    // + 점수가 들어왔을 때 Animator
    private fun plusAnimator(view: TextView): ValueAnimator {
        return ValueAnimator.ofFloat(0.5f, 2.0f, 1.0f).apply {
            duration = 600L
            addUpdateListener { animation ->
                view.scaleX = animation.animatedValue as Float
                view.scaleY = animation.animatedValue as Float
            }
        }
    }

    // - 점수가 들어왔을 때 Animator
    private fun minusAnimator(view: TextView): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 10f, 0f, -10f, 0f).apply {
            repeatCount = 20
            duration = 40L
        }
    }
}