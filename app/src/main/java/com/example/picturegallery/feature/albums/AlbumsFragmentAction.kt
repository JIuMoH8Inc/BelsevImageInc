package com.example.picturegallery.feature.albums

import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.dialog.InputTwoStringsDialog

sealed interface AlbumsFragmentAction {
    data class OpenDeleteConfirmDialog(val data: AnswerDialog.DialogData) : AlbumsFragmentAction
    data class OpenChangeTitleAndDescDialog(val data: InputTwoStringsDialog.InputData) :
        AlbumsFragmentAction

    data class OpenChangeCoverDialog(val albumId: Int) : AlbumsFragmentAction

    data class OpenAlbum(val id: Int, val name: String) : AlbumsFragmentAction
    object OpenCreateAlbumFragment : AlbumsFragmentAction
}