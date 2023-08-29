package com.example.picturegallery.feature.albums.action

import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState

sealed interface AlbumsFragmentAction {
    data class OpenAlbumDetailsBottomSheet(val album: AlbumAdapterUiState) : AlbumsFragmentAction
    data class OpenAlbum(val id: Int) : AlbumsFragmentAction
}