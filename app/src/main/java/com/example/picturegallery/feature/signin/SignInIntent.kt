package com.example.picturegallery.feature.signin

sealed interface SignInIntent {
    data class OnChangeLogin(val login: String) : SignInIntent
    data class OnChangePassword(val password: String) : SignInIntent
    object OnSignInButtonClick : SignInIntent
}