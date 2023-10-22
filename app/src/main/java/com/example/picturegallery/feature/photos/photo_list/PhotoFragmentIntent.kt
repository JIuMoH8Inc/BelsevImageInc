package com.example.picturegallery.feature.photos.photo_list


sealed interface PhotoFragmentIntent {
    data class OnLoadPhotoList(val isInitLoading: Boolean, val offset: Int) : PhotoFragmentIntent

    data class OnPhotoClick(val id: Int) : PhotoFragmentIntent
}