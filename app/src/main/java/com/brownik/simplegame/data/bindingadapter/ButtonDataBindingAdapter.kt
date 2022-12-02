package com.brownik.simplegame.data.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.brownik.simplegame.R

object ButtonDataBindingAdapter {
    @BindingAdapter("app:changeImageResource")
    @JvmStatic
    fun changeButtonScore(targetView: ImageView, state: Int) {
        val image = when (state) {
            -5 -> R.drawable.minus_5
            -4 -> R.drawable.minus_4
            -3 -> R.drawable.minus_3
            -2 -> R.drawable.minus_2
            -1 -> R.drawable.minus_1
            0 -> null
            1 -> R.drawable.plus_1
            2 -> R.drawable.plus_2
            3 -> R.drawable.plus_3
            4 -> R.drawable.plus_4
            5 -> R.drawable.plus_5
            else -> null
        }
        if (image != null) targetView.setImageResource(image)
        else targetView.setImageResource(0)
    }
}