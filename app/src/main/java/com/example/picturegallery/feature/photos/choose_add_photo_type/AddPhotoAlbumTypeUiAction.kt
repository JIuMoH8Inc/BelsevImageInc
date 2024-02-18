package com.example.picturegallery.feature.photos.choose_add_photo_type

sealed interface AddPhotoAlbumTypeUiAction {
    data class OpenCreateNewAlbumClick(val photoList: IntArray) : AddPhotoAlbumTypeUiAction
    data class OpenExistAlbumsFragment(val photoList: IntArray) : AddPhotoAlbumTypeUiAction
}