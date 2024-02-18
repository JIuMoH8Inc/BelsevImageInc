package com.example.picturegallery.feature.album_content

import com.example.picturegallery.feature.photos.PhotosAdapterUiState
import com.example.picturegallery.ui.views.EmptyUi

data class AlbumContentUiState(
    val toolbarTitle: String = "",
    val photoList: List<PhotosAdapterUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isSelectionMode: Boolean = false,
    val errorUi: EmptyUi? = null
)
