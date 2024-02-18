package com.example.picturegallery.feature.photos.choose_add_photo_type

import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.flow.SingleFlow
import javax.inject.Inject

class AddPhotoAlbumTypeViewModel @Inject constructor(
) : BaseViewModel() {
    private val _uiAction = SingleFlow<AddPhotoAlbumTypeUiAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var selectedPhoto: List<Int> = emptyList()

    fun handleIntent(intent: AddPhotoAlbumTypeIntent) {
        when (intent) {
            is AddPhotoAlbumTypeIntent.OnParseArgs -> parseArgs(intent.photoList)
            AddPhotoAlbumTypeIntent.OnCreateNewAlbumClick -> {
                _uiAction.tryEmit(
                    AddPhotoAlbumTypeUiAction.OpenCreateNewAlbumClick(selectedPhoto.toIntArray())
                )
            }

            AddPhotoAlbumTypeIntent.OnExistAlbumClick -> {
                _uiAction.tryEmit(
                    AddPhotoAlbumTypeUiAction.OpenExistAlbumsFragment(selectedPhoto.toIntArray())
                )
            }
        }
    }

    private fun parseArgs(photoList: List<Int>) {
        selectedPhoto = photoList
    }
}