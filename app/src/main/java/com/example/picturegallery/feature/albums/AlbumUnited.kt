package com.example.picturegallery.feature.albums

import com.example.picturegallery.domain.model.album.Album

data class AlbumUnited(
    val albums: List<Album> = emptyList(),
    val albumsUi: List<AlbumAdapterUiState> = emptyList()
)
