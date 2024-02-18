package com.example.picturegallery.feature.photos.choose_add_photo_type

sealed interface AddPhotoAlbumTypeIntent {
    data class OnParseArgs(val photoList: List<Int>) : AddPhotoAlbumTypeIntent
    object OnCreateNewAlbumClick : AddPhotoAlbumTypeIntent
    object OnExistAlbumClick : AddPhotoAlbumTypeIntent
}