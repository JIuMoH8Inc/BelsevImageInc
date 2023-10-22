package com.example.picturegallery.feature.photos.photo_view

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.manager.ImageManager
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.ui.views.EmptyUi
import com.example.picturegallery.utils.extensions.launchRequest
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

    fun handleIntent(intent: ViewPhotoIntent) {
        when (intent) {
            is ViewPhotoIntent.OnParseArgs -> {
                load(intent.id)
            }
        }
    }

    private fun load(id: Int) {
        viewModelScope.launchRequest(
            request = {
                photosRepository.getPhotoFile(id)
            },
            onSuccess = { photoBytes ->
                _uiState.update {
                    it.copy(
                        photo = BitmapFactory.decodeByteArray(
                            photoBytes,
                            0,
                            photoBytes.size
                        ),
                        isNeedToRotate = imageManager.isNeedToRotate(photoBytes)
                    )
                }
            },
            onError = {
                _uiState.update {
                    it.copy(
                        errorUi = EmptyUi(
                            header = R.string.view_photo_load_error_header,
                            subtitle = R.string.try_again_later,
                            buttonText = R.string.refresh,
                            onButtonClick = {
                                load(id)
                            }
                        )
                    )
                }
            }
        )
    }

}