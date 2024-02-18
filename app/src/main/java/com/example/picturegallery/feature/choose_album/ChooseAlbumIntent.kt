package com.example.picturegallery.feature.choose_album

sealed interface ChooseAlbumIntent {
    data class OnParseArgs(val photoList: IntArray) : ChooseAlbumIntent
    data class OnAlbumClick(val albumId: Int) : ChooseAlbumIntent
}