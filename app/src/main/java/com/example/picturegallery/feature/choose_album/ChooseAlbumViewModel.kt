package com.example.picturegallery.feature.choose_album

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.EditAlbumPhotosRequest
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.domain.useCase.GetAlbumUseCase
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val albumUseCase: GetAlbumUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(
        ChooseAlbumUiState()
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<ChooseAlbumUiAction>()
    val uiAction = _uiAction.asSharedFlow()
    private var selectedPhotos = emptyList<Int>()
    private var albumList = emptyList<Album>()
    private var selectedName = ""

    fun handleIntent(intent: ChooseAlbumIntent) {
        when (intent) {
            is ChooseAlbumIntent.OnAlbumClick -> {
                selectedName = albumList.first { it.id == intent.albumId }.name
                addToAlbum(
                    EditAlbumPhotosRequest(
                        intent.albumId,
                        selectedPhotos
                    )
                )
            }

            is ChooseAlbumIntent.OnParseArgs -> {
                selectedPhotos = intent.photoList.toList()
                load()
            }
        }
    }

    private fun load() = viewModelScope.launchRequest(
        request = {
            albumUseCase()
        },
        onLoading = { isLoading ->
            _uiState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        },
        onSuccess = { (albums, albumsUi) ->
            albumList = albums
            _uiState.update {
                it.copy(
                    isLoading = false,
                    albumList = albumsUi
                )
            }
        }
    )

    private fun addToAlbum(request: EditAlbumPhotosRequest) = viewModelScope.launchRequest(
        request = {
            albumRepository.addToAlbum(request)
        },
        onSuccess = {
            showSuccessBanner(resourceManager.getString(R.string.add_to_album_success))
            _uiAction.tryEmit(
                ChooseAlbumUiAction.OpenAlbumContent(request.albumId, selectedName)
            )
        },
        onError = {
            showErrorBanner(resourceManager.getString(R.string.add_to_album_error))
            _uiAction.tryEmit(
                ChooseAlbumUiAction.DismissDialog
            )
        }
    )
}