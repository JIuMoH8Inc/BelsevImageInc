package com.example.picturegallery.feature.photos.uiAction

import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState
import com.example.picturegallery.ui.dialog.AnswerDialog

sealed interface PhotosUiAction {
    data class ShowErrorBanner(val message: String) : PhotosUiAction
    data class ShowSuccessBanner(val message: String) : PhotosUiAction
    data class ShowAnswerDialog(val dialogData: AnswerDialog.DialogData) : PhotosUiAction
    data class SelectPhotos(
        val list: List<PhotosAdapterUiState>,
        val isSelectionMode: Boolean,
        val toolbarTitle: String) : PhotosUiAction

}