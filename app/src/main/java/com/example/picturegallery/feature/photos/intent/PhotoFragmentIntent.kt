package com.example.picturegallery.feature.photos.intent

sealed interface PhotoFragmentIntent {
    data class OnLoadPhotoList(val isInitLoading: Boolean, val offset: Int) : PhotoFragmentIntent
    data class OnPhotoClick(val id: Int) : PhotoFragmentIntent
    data class OnSelectPhoto(val id: Int, val isSelected: Boolean) : PhotoFragmentIntent
    object OnDeleteMenuClick : PhotoFragmentIntent
}