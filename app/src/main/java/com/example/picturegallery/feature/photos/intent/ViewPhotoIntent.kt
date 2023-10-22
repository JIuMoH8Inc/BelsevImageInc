package com.example.picturegallery.feature.photos.intent

sealed interface ViewPhotoIntent {
    object OnTryAgainButtonClick: ViewPhotoIntent
}