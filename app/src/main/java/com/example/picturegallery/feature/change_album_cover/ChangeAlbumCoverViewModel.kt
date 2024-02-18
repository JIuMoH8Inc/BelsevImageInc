package com.example.picturegallery.feature.change_album_cover

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.domain.useCase.GetPhotoUiUseCase
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangeAlbumCoverViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val getPhotoUiUseCase: GetPhotoUiUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(
        ChangeAlbumCoverUiState()
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<ChangeAlbumCoverUiAction>()
    val uiAction = _uiAction.asSharedFlow()

    fun handleIntent(intent: ChangeAlbumCoverIntent) {
        when (intent) {
            is ChangeAlbumCoverIntent.OnParseArgs -> {
                loadAlbumPhotos(intent.albumId)
            }

            is ChangeAlbumCoverIntent.OnPhotoClick -> {
                _uiAction.tryEmit(
                    ChangeAlbumCoverUiAction.OnPhotoChoose(intent.id)
                )
            }
        }
    }

    private fun loadAlbumPhotos(id: Int) = viewModelScope.launchRequest(
        request = {
            albumRepository.getAlbumPhotos(id)
        },
        onLoading = { isLoading ->
            _uiState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        },
        onSuccess = { photos ->
            _uiState.update {
                it.copy(
                    photoList = getPhotoUiUseCase(photos.list)
                )
            }
        }
    )
}