package com.example.picturegallery.feature.album_content

sealed interface AlbumContentIntent {
    data class OnParseArguments(val albumId: Int, val albumName: String) : AlbumContentIntent
    data class OnPhotoClick(val id: Int) : AlbumContentIntent
    data class OnSelectPhoto(val id: Int, val isSelected: Boolean) : AlbumContentIntent
    object OnDeleteMenuClick : AlbumContentIntent
    data class OnAnswerHandle(val result: Boolean) : AlbumContentIntent
    object OnAddPhotoToAlbumClick : AlbumContentIntent
    data class OnAddAlbumList(val ids: List<Int>) : AlbumContentIntent
}