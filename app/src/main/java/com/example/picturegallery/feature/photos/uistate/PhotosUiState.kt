package com.example.picturegallery.feature.photos.uistate

data class PhotosUiState(
    val toolbarTitle: String = "",
    val photoList: List<PhotosAdapterUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isEmpty: Boolean = false
)
