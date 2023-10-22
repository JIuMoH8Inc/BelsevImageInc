package com.example.picturegallery.feature.photos.photo_view

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.example.picturegallery.domain.manager.ImageManager
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


const val PHOTO_ID_KEY = "PHOTO_ID_KEY"
class ViewPhotoViewModel @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val imageManager: ImageManager
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(
        ViewPhotoUiState()
    )
    val uiState = _uiState.asStateFlow()

    private var id = 0

    fun handleIntent(intent: ViewPhotoIntent) {
        when (intent) {
            ViewPhotoIntent.OnTryAgainButtonClick -> {
                load()
            }
        }
    }

    fun parseArguments(bundle: Bundle) {
        id = bundle.getInt(PHOTO_ID_KEY)
        load()
    }

    private fun load() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isError = false
                )
            }
            kotlin.runCatching {
                photosRepository.getPhotoFile(id)
            }
                .onSuccess { photo ->
                    val bytes = photo.bytes()
                    _uiState.update {
                        it.copy(
                            photo = BitmapFactory.decodeByteArray(
                                bytes,
                                0,
                                bytes.size
                            ),
                            isNeedToRotate = imageManager.isNeedToRotate(bytes)
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isError = true
                        )
                    }
                }
        }

}