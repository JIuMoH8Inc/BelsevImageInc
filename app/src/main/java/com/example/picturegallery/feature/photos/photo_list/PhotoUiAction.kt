package com.example.picturegallery.feature.photos.photo_list


sealed interface PhotoUiAction {
    data class OpenViewPhotoFragment(val id: Int) : PhotoUiAction
}