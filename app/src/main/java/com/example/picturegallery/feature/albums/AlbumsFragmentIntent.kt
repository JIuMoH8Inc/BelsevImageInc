package com.example.picturegallery.feature.albums


sealed interface AlbumsFragmentIntent {
    data class OnAlbumClick(val id: Int) : AlbumsFragmentIntent
    data class OnMoreItemClick(val itemId: Int, val albumId: Int) : AlbumsFragmentIntent
    object OnLoadAlbums : AlbumsFragmentIntent
    object OnAddAlbumClick : AlbumsFragmentIntent
    data class OnChoosePhoto(val id: Int) : AlbumsFragmentIntent
    data class OnHandleAnswerResult(val result: Boolean) : AlbumsFragmentIntent
    data class OnHandleInputResult(val first: String, val second: String) : AlbumsFragmentIntent
}