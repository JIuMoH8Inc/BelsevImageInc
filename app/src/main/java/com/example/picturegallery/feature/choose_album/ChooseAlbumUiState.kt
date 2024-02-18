package com.example.picturegallery.feature.choose_album

import com.example.picturegallery.feature.albums.AlbumAdapterUiState

data class ChooseAlbumUiState(
    val isLoading: Boolean = false,
    val albumList: List<AlbumAdapterUiState> = emptyList()
)