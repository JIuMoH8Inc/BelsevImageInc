package com.example.picturegallery.feature.photos.intent

sealed interface PhotoFragmentIntent {
    object OnLoadPhotoList : PhotoFragmentIntent
}