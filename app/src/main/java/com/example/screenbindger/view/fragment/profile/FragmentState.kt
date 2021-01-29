package com.example.screenbindger.view.fragment.profile

import java.lang.Exception

sealed class FragmentState(var editable: Boolean) {
    object Editable : FragmentState(true)
    object NotEditable : FragmentState(false)
    object UpdateSuccess : FragmentState(false)
    data class UpdateFail(val exception: Exception) :
        FragmentState(false)
}