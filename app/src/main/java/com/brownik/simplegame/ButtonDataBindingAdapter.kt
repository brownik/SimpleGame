package com.brownik.simplegame

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ButtonDataBindingAdapter {
    @BindingAdapter("app:changeImageResource")
    @JvmStatic
    fun changeImageResource(targetView: ImageView, state: Int) {
        val image = when(state){
            0 -> null
            1 -> R.drawable.icon_x
            2 -> R.drawable.icon_o
            else -> null
        }
        if (image != null) targetView.setImageResource(image)
        else targetView.setImageResource(0)
    }
}