package com.brownik.simplegame.data.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

object ScoreDataBindingAdapter {
    @BindingAdapter("app:setScore")
    @JvmStatic
    fun setScore(targetView: TextView, score: Int) {
        targetView.text = score.toString()
    }
}