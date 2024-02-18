package com.example.picturegallery.feature.create_album

import com.example.picturegallery.feature.photos.PhotosAdapterUiState
import com.example.picturegallery.ui.views.EmptyUi

data class CreateAlbumUiState(
    val toolbarTitle: String = "",
    val photoList: List<PhotosAdapterUiState> = emptyList(),
    val isLoading: Boolean = false,
    val emptyNameError: String? = null,
    val isEmptyPhotoList: Boolean = false,
    val errorUi: EmptyUi? = null,
    val isPhotoHeaderVisible: Boolean = true
)