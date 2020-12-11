package com.example.screenbindger.view.custom.edittext

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.screenbindger.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("AppCompatCustomView")
class DatePickerEditText
@JvmOverloads
constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : TextInputEditText(context, attributeSet, defStyleAttr) {

    private lateinit var datePicker: MaterialDatePicker<*>

    init {
        showSoftInputOnFocus = false
    }

/*    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if(focused){
            showDatePicker()
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        showDatePicker()
    }*/


    fun showDatePicker(parentFragmentManager: FragmentManager) {
        datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Select your date of birth")
            .build()
        val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        datePicker.addOnPositiveButtonClickListener {
            val text = outputDateFormat.format(it)
            datePicker.headerText
            setText(text)
            clearFocus()
        }

        datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

    }
}