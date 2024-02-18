package com.example.picturegallery.feature.signin

sealed interface SignInAction {
    object OpenAlbumFragment : SignInAction
}