package com.example.screenbindger.util.extensions

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.show(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    this.visibility = View.GONE
}