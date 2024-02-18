package com.example.picturegallery.feature.photos

import com.example.picturegallery.ui.dialog.AnswerDialog

sealed interface PhotosUiAction {
    data class ShowAnswerDialog(val dialogData: AnswerDialog.DialogData) : PhotosUiAction
    data class SelectPhotos(
        val list: List<PhotosAdapterUiState>,
        val isSelectionMode: Boolean,
        val toolbarTitle: String,
        val isAddToAlbum: Boolean
    ) : PhotosUiAction


    data class AddPhotosToAlbum(val selectedIdList: List<Int>) : PhotosUiAction

    data class OpenChooseAlbumType(val selectedPhotos: IntArray) : PhotosUiAction

}