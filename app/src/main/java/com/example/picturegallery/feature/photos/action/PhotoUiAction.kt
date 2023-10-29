package com.example.picturegallery.feature.photos.action


sealed interface PhotoUiAction {
    data class OpenViewPhotoFragment(val id: Int) : PhotoUiAction
}