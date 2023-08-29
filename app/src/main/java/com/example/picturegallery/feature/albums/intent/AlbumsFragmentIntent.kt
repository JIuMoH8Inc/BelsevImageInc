package com.example.picturegallery.feature.albums.intent

import com.example.picturegallery.feature.albums.uistate.AlbumAdapterUiState

sealed interface AlbumsFragmentIntent {
    data class OnAlbumClick(val id: Int) : AlbumsFragmentIntent
    data class OnMoreClick(val album: AlbumAdapterUiState) : AlbumsFragmentIntent
    object OnLoadAlbums : AlbumsFragmentIntent
}