package com.example.picturegallery.feature.signin.action

sealed interface SignInAction {
    object OpenAlbumFragment : SignInAction
}