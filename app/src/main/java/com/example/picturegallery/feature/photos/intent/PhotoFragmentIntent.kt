package com.example.picturegallery.feature.photos.intent

sealed interface PhotoFragmentIntent {
    data class OnLoadPhotoList(val isInitLoading: Boolean, val offset: Int) : PhotoFragmentIntent
}