package com.example.screenbindger.util.extensions

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.example.screenbindger.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.setIconAndColor(drawableResId: Int, colorResId: Int) {
    setImageResource(drawableResId)
    val color = ContextCompat.getColor(context, colorResId)
    backgroundTintList = ColorStateList.valueOf(color)
}