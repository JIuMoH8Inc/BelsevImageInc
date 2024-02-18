package com.example.picturegallery.feature.albums

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.UpdateAlbumRequest
import com.example.picturegallery.domain.model.album.UpdateCoverRequest
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.domain.useCase.GetAlbumUseCase
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.dialog.InputTwoStringsDialog
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.ui.views.EmptyUi
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase,
    private val albumRepository: AlbumRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        AlbumsFragmentUiState(toolbarTitle = resourceManager.getString(R.string.album_toolbar_title))
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<AlbumsFragmentAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var albumList: List<Album> = emptyList()
    private var selectedAlbumId = -1
    private var selectedAlbumTitle = ""
    private var selectedAlbumDesc = ""

    private fun load() = viewModelScope.launchRequest(
        request = {
            getAlbumUseCase()
        },
        onLoading = { isLoading ->
            _uiState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        },
        onSuccess = { (albums, albumUi) ->
            albumList = albums
            _uiState.update {
                it.copy(
                    albumList = albumUi,
                    emptyUi = getEmptyState()
                )
            }
        },
        onError = {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = false
                )
            }
        }
    )

    fun handleIntent(intent: AlbumsFragmentIntent) {
        when (intent) {
            is AlbumsFragmentIntent.OnAlbumClick -> {
                selectedAlbumTitle = albumList.first { it.id == intent.id }.name
                _uiAction.tryEmit(
                    AlbumsFragmentAction.OpenAlbum(intent.id, selectedAlbumTitle)
                )
            }

            is AlbumsFragmentIntent.OnMoreItemClick -> {
                selectedAlbumId = intent.albumId
                selectedAlbumTitle = albumList.first { it.id == selectedAlbumId }.name
                selectedAlbumDesc = albumList.first { it.id == selectedAlbumId }.description
                handleMenuItemClick(intent.itemId)
            }

            AlbumsFragmentIntent.OnLoadAlbums -> {
                load()
            }

            AlbumsFragmentIntent.OnAddAlbumClick -> {
                _uiAction.tryEmit(
                    AlbumsFragmentAction.OpenCreateAlbumFragment
                )
            }

            is AlbumsFragmentIntent.OnChoosePhoto -> {
                updateCover(intent.id)
            }

            is AlbumsFragmentIntent.OnHandleAnswerResult -> {
                if (intent.result) {
                    deleteAlbum()
                }
            }

            is AlbumsFragmentIntent.OnHandleInputResult -> {
                updateAlbum(
                    UpdateAlbumRequest(
                        albumId = selectedAlbumId,
                        name = intent.first,
                        description = intent.second
                    )
                )
            }
        }
    }

    private fun handleMenuItemClick(itemId: Int) = when (itemId) {
        R.id.delete_album -> {
            _uiAction.tryEmit(
                AlbumsFragmentAction.OpenDeleteConfirmDialog(
                    AnswerDialog.DialogData(
                        header = resourceManager.getString(R.string.delete_album_answer_header),
                        answer = resourceManager.getString(
                            R.string.delete_album_answer_text,
                            selectedAlbumTitle
                        ),
                        positiveButtonText = resourceManager.getString(R.string.delete),
                        negativeButtonText = resourceManager.getString(R.string.cancel)
                    )
                )
            )
        }

        R.id.change_cover -> {
            _uiAction.tryEmit(
                AlbumsFragmentAction.OpenChangeCoverDialog(
                    selectedAlbumId
                )
            )
        }

        R.id.change_title -> {
            _uiAction.tryEmit(
                AlbumsFragmentAction.OpenChangeTitleAndDescDialog(
                    InputTwoStringsDialog.InputData(
                        header = R.string.change_album_data,
                        firstInputHint = R.string.album_name_hint,
                        secondInputHint = R.string.album_desc_hint,
                        negativeButtonText = R.string.cancel,
                        initFirstInput = selectedAlbumTitle,
                        initSecondInput = selectedAlbumDesc,
                        positiveButtonText = R.string.change
                    )
                )
            )
        }

        else -> {}
    }

    private fun deleteAlbum() = viewModelScope.launchRequest(
        request = {
            albumRepository.deleteAlbum(selectedAlbumId)
        },
        onSuccess = {
            showSuccessBanner(
                resourceManager.getString(R.string.delete_album_success, selectedAlbumTitle)
            )
            load()
        },
        onError = {
            showErrorBanner(
                resourceManager.getString(
                    R.string.delete_album_error,
                    selectedAlbumTitle
                )
            )
        }
    )

    private fun updateAlbum(request: UpdateAlbumRequest) = viewModelScope.launchRequest(
        request = {
            albumRepository.updateAlbum(request)
        },
        onSuccess = {
            showSuccessBanner(resourceManager.getString(R.string.change_album_data_success))
            load()
        },
        onError = {
            showErrorBanner(resourceManager.getString(R.string.change_album_data_error))
        }
    )

    private fun updateCover(id: Int) = viewModelScope.launchRequest(
        request = {
            albumRepository.updateAlbumCover(
                UpdateCoverRequest(
                    albumId = selectedAlbumId,
                    coverId = id
                )
            )
        },
        onSuccess = {
            showSuccessBanner(
                resourceManager.getString(R.string.change_cover_success)
            )
            load()
        },
        onError = {
            showErrorBanner(resourceManager.getString(R.string.change_cover_error))
        }
    )

    private fun getEmptyState(): EmptyUi? = if (albumList.isEmpty()) {
        EmptyUi(
            onButtonClick = {
                _uiAction.tryEmit(
                    AlbumsFragmentAction.OpenCreateAlbumFragment
                )
            }
        )
    } else null
}