package com.example.screenbindger.util.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.example.screenbindger.R
import com.google.android.material.snackbar.Snackbar

fun View.snack(
    @StringRes messageRes: Int,
    @ColorRes backgroundColorRes: Int,
    length: Int = Snackbar.LENGTH_LONG
) {
    snack(resources.getString(messageRes), backgroundColorRes, length)
}

fun View.snack(
    message: String,
    backgroundColorRes: Int = R.color.blue,
    length: Int = Snackbar.LENGTH_LONG
) {

    val snack = Snackbar.make(this, message, length)

    val color =
        ResourcesCompat.getColor(resources, backgroundColorRes, null)
    snack.setBackgroundTint(color)

    snack.show()
}