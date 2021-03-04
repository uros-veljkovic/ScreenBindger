package com.example.screenbindger.util.extensions

import android.view.View

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun disable(vararg views: View) {
    views.forEach {
        it.isEnabled = false
        it.isClickable = false
    }
}


fun enable(vararg views: View) {
    views.forEach {
        it.isEnabled = true
        it.isClickable = true
    }
}