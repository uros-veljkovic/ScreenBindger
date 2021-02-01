package com.example.screenbindger.view.fragment.profile

import java.lang.Exception

sealed class FragmentState() {
    object Editable : FragmentState()
    object NotEditable : FragmentState()
}