package com.example.picturegallery.feature.change_album_cover

sealed interface ChangeAlbumCoverIntent {
    data class OnParseArgs(val albumId: Int) : ChangeAlbumCoverIntent
    data class OnPhotoClick(val id: Int) : ChangeAlbumCoverIntent
}