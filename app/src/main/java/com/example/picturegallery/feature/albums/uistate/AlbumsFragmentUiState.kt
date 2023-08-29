package com.example.picturegallery.feature.albums.uistate

data class AlbumsFragmentUiState(
    val toolbarTitle: String = "",
    val albumList: List<AlbumAdapterUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
