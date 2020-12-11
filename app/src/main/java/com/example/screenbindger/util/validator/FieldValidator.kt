package com.example.screenbindger.util.validator

import android.view.View
import bloder.com.blitzcore.validation.DefaultBlitzValidations
import com.google.android.material.textfield.TextInputEditText

class FieldValidator : DefaultBlitzValidations() {

    fun TextInputEditText.validateFrom(regex: Regex): View = bindViewValidation(this) {
        this.text?.matches(regex)!!
    }

    fun TextInputEditText.isNotEmpty(): View = bindViewValidation(this) {
        this.text?.isNotEmpty()!!
    }

    fun TextInputEditText.hasNoError(): View = bindViewValidation(this) {
        this.error == null
    }

}

infix fun TextInputEditText.matches(et: TextInputEditText): Boolean {
    return this.text!! == et.text!!
}