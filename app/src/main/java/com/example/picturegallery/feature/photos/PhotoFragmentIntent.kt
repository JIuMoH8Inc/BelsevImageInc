package com.example.picturegallery.feature.photos

sealed interface PhotoFragmentIntent {
    data class OnLoadPhotoList(val isInitLoading: Boolean, val offset: Int) : PhotoFragmentIntent
    data class OnParseArgs(val isAddToAlbum: Boolean) : PhotoFragmentIntent
    data class OnPhotoClick(val id: Int) : PhotoFragmentIntent
    data class OnSelectPhoto(val id: Int, val isSelected: Boolean) : PhotoFragmentIntent
    object OnDeleteMenuClick : PhotoFragmentIntent
    object OnToAlbumMenuClick : PhotoFragmentIntent
    data class OnAnswerHandle(val result: Boolean) : PhotoFragmentIntent
    object OnAddToAlbumClick : PhotoFragmentIntent
}