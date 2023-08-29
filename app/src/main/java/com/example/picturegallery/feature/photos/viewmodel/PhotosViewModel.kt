package com.example.picturegallery.feature.photos.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.useCase.GetPhotosUseCase
import com.example.picturegallery.feature.photos.intent.PhotoFragmentIntent
import com.example.picturegallery.feature.photos.uistate.PhotosUiState
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(PhotosUiState(
        toolbarTitle = resourceManager.getString(R.string.photo_toolbar_title))
    )
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: PhotoFragmentIntent) {
        when (intent) {
            PhotoFragmentIntent.OnLoadPhotoList -> {
                load()
            }
        }
    }

    private fun load() {
        _uiState.update {
            it.copy(
                isLoading = true,
                isError = false,
                isEmpty = false
            )
        }
        viewModelScope.launch {
            kotlin.runCatching {
                getPhotosUseCase()
            }
                .onSuccess { photos ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            photoList = photos
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
        }
    }
}