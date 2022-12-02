package com.brownik.simplegame.data.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

object RemainTimeDataBindingAdapter {
    @BindingAdapter("app:setRemainTime")
    @JvmStatic
    fun setRemainTime(targetView: TextView, state: Int) {
        targetView.text = "${state}초"
    }
}