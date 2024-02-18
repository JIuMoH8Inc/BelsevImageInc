package com.example.picturegallery.feature.choose_album

sealed interface ChooseAlbumUiAction {
    data class OpenAlbumContent(val id: Int, val name: String) : ChooseAlbumUiAction
    object DismissDialog : ChooseAlbumUiAction
}