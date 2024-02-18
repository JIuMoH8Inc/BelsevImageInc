package com.example.picturegallery.feature.create_album

sealed interface CreateAlbumIntent {
    object LoadPhotos : CreateAlbumIntent

    data class ParseArgs(val photoList: List<Int>?) : CreateAlbumIntent
    data class OnSelectPhoto(val id: Int, val isSelected: Boolean) : CreateAlbumIntent

    data class OnTitleChange(val title: String) : CreateAlbumIntent
    data class OnDescChange(val desc: String) : CreateAlbumIntent
    object OnCreateAlbumClick : CreateAlbumIntent
}