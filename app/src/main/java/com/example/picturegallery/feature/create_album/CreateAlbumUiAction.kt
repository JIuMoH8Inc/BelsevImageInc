package com.example.picturegallery.feature.create_album

import com.example.picturegallery.feature.photos.PhotosAdapterUiState

sealed interface CreateAlbumUiAction {
    data class SelectPhotos(val photos: List<PhotosAdapterUiState>) : CreateAlbumUiAction
    data class SetEmptyNameError(val error: String?) : CreateAlbumUiAction
    data class OpenNewAlbum(val id: Int, val name: String) : CreateAlbumUiAction
}