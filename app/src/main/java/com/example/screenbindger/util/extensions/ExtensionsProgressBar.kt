package com.example.screenbindger.util.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.screenbindger.R

fun ProgressBar.show() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    this.visibility = View.GONE
}

@BindingAdapter("app:progressFromDouble")
fun ProgressBar.setProgress(value: Double?) {
    val v = (value?.times(10))?.toInt() ?: 0
    progress = v
}

@BindingAdapter("app:progressBarColor")
fun ProgressBar.setProgressBarColor(value: Double?) {
    val percentValue = value?.times(10)

    percentValue?.let {
        val red = ContextCompat.getColor(context, R.color.logout_red)
        val orange = ContextCompat.getColor(context, R.color.orange)
        val yellow = ContextCompat.getColor(context, R.color.yellow)
        val green = ContextCompat.getColor(context, R.color.green)

        indeterminateTintList = if (it isLessOrEqualTo 25.0) {
            ColorStateList.valueOf(red)
        } else if (it isGreaterThan 25.0 && it isLessOrEqualTo 50.0) {
            ColorStateList.valueOf(orange)
        } else if (it isGreaterThan 50.0 && it isLessOrEqualTo 75.0) {
            ColorStateList.valueOf(yellow)
        } else {
            ColorStateList.valueOf(green)
        }
    }
}