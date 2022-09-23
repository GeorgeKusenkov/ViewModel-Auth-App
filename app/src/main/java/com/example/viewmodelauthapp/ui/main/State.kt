package com.example.viewmodelauthapp.ui.main

sealed class State {
    object Loading: State()
    object Success: State()
    data class Error(
        val loginError: String?,
        val passwordError: String?,
    ): State()
}
