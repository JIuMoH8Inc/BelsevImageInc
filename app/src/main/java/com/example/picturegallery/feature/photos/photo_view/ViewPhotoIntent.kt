package com.example.picturegallery.feature.photos.photo_view

sealed interface ViewPhotoIntent {
    object OnTryAgainButtonClick: ViewPhotoIntent
}