package com.example.picturegallery.feature.album_content

import com.example.picturegallery.feature.photos.PhotosAdapterUiState
import com.example.picturegallery.ui.dialog.AnswerDialog

sealed interface AlbumContentUiAction {
    data class SelectPhotos(
        val list: List<PhotosAdapterUiState>,
        val isSelectionMode: Boolean,
        val toolbarTitle: String) : AlbumContentUiAction
    data class ShowAnswerDialog(val dialogData: AnswerDialog.DialogData) : AlbumContentUiAction
    object OpenChoosePhotoFragment : AlbumContentUiAction
}