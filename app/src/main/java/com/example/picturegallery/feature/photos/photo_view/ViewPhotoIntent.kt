package com.example.picturegallery.feature.photos.photo_view

sealed interface ViewPhotoIntent {
    data class OnParseArgs(val id: Int) : ViewPhotoIntent
}