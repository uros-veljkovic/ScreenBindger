package com.example.screenbindger.util.extensions

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:textPercentage")
fun TextView.setTextPercentage(value: Double?) {
    val percentage = value?.times(10)?.toInt() ?: 0
    text = if (percentage == 0) {
        "?"
    } else {
        val percentageText = percentage.toString()
        "${percentageText}%"
    }
}