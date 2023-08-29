package com.example.picturegallery.feature.signin.intent

sealed interface SignInIntent {
    data class OnChangeLogin(val login: String) : SignInIntent
    data class OnChangePassword(val password: String) : SignInIntent
    object OnSignInButtonClick : SignInIntent
}