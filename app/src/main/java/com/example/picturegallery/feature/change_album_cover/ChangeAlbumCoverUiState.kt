package com.example.picturegallery.feature.change_album_cover

import com.example.picturegallery.feature.photos.PhotosAdapterUiState

data class ChangeAlbumCoverUiState(
    val isLoading: Boolean = false,
    val photoList: List<PhotosAdapterUiState> = emptyList()
)
