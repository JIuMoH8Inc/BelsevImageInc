package com.example.picturegallery.feature.signin.uistate

import android.support.annotation.StringRes
import com.example.picturegallery.R

data class SignInUiState(
    val login: String = "",
    @StringRes val loginHint: Int = R.string.login,
    val password: String = "",
    @StringRes val passwordHint: Int = R.string.password,
    val isLoading: Boolean = false
)
