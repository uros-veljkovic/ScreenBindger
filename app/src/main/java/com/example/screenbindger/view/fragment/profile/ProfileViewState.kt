package com.example.screenbindger.view.fragment.profile

sealed class ProfileViewState() {
    object Editable : ProfileViewState()
    object NotEditable : ProfileViewState()
}