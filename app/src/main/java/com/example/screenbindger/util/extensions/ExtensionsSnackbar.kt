package com.example.screenbindger.util.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
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
    backgroundColorRes: Int,
    length: Int = Snackbar.LENGTH_LONG
) {
    val color =
        ResourcesCompat.getColor(resources, backgroundColorRes, null)

    val snack = Snackbar.make(this, message, length).setBackgroundTint(color)
    snack.show()
}