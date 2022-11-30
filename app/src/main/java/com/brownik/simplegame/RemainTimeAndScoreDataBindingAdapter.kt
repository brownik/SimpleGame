package com.brownik.simplegame

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

object RemainTimeAndScoreDataBindingAdapter {
    @BindingAdapter("app:setRemainTime")
    @JvmStatic
    fun setRemainTime(targetView: TextView, state: Int) {
        targetView.text = "${state}초"
    }
}