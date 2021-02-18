package com.example.screenbindger.view.fragment.profile

sealed class ProfileFragmentState() {
    object Editable : ProfileFragmentState()
    object NotEditable : ProfileFragmentState()
}