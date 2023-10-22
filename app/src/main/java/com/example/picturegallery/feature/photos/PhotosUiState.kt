package com.example.picturegallery.feature.photos

data class PhotosUiState(
    val toolbarTitle: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isEmpty: Boolean = false,
    val isLastPage: Boolean = true,
    val isNextPageLoading: Boolean = false,
    val isSelectionMode: Boolean = false,
    val isAddToAlbum: Boolean = false
)
