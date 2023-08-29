package com.example.picturegallery.ui.fragment.base

sealed interface BaseErrorAction {
    data class ShowErrorBanner(val message: String) : BaseErrorAction
}