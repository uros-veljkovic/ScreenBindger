package com.example.screenbindger.util.extensions

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.example.screenbindger.R
import com.example.screenbindger.view.activity.main.MainActivity
import kotlin.reflect.KClass

fun Fragment.startActivityWithDelay(activity: Activity, millis: Long) {
    Handler(Looper.myLooper()!!).postDelayed({

        startActivity(Intent(requireActivity(), activity::class.java))
        requireActivity().finish()
    }, millis)
}

fun Fragment.snackbar(message: String, colorResId: Int = R.color.design_default_color_background) {
    requireView().snack(message, colorResId)
}

fun Fragment.snackbar(stringResId: Int, colorResId: Int = R.color.design_default_color_background) {
    requireView().snack(stringResId, colorResId)
}