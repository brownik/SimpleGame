package com.brownik.simplegame.data.bindingadapter

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter

object RemainTimeDataBindingAdapter {
    @BindingAdapter("app:setRemainTime")
    @JvmStatic
    fun setRemainTime(targetView: TextView, score: Int) {
        if (score < 10) targetView.setTextColor(Color.parseColor("#FF0000"))
        else targetView.setTextColor(Color.parseColor("#576F72"))
        targetView.text = "${score}초"
    }
}