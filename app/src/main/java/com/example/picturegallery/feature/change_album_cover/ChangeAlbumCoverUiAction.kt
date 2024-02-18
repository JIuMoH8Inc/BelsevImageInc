package com.example.picturegallery.feature.change_album_cover

sealed interface ChangeAlbumCoverUiAction {
    data class OnPhotoChoose(val id: Int) : ChangeAlbumCoverUiAction
}