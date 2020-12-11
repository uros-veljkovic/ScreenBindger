package com.example.screenbindger.util.extensions

import android.view.View
import android.widget.EditText

inline fun EditText.onFocusChange(crossinline hasFocus: (Boolean) -> Unit) {
    setOnFocusChangeListener(View.OnFocusChangeListener { view, gotFocus -> })
}