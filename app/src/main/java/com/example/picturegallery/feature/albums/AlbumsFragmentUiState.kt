package com.example.picturegallery.feature.albums

import com.example.picturegallery.ui.views.EmptyUi

data class AlbumsFragmentUiState(
    val toolbarTitle: String = "",
    val albumList: List<AlbumAdapterUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val emptyUi: EmptyUi? = null
)
