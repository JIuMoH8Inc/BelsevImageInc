package com.example.picturegallery.feature.album_content

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.model.album.EditAlbumPhotosRequest
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.domain.useCase.GetPhotoUiUseCase
import com.example.picturegallery.feature.photos.PhotosAdapterUiState
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.ui.views.EmptyUi
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumContentViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val getPhotoUiUseCase: GetPhotoUiUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(AlbumContentUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<AlbumContentUiAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var albumId = -1
    private var albumName = ""

    private var selectedPhotosId = mutableListOf<Int>()
    private var photoList = emptyList<PhotosAdapterUiState>()
    private val isSelectionMode: Boolean
        get() = selectedPhotosId.isNotEmpty()

    fun handleIntent(intent: AlbumContentIntent) {
        when (intent) {
            is AlbumContentIntent.OnParseArguments -> {
                parseArgs(intent.albumId, intent.albumName)
            }

            AlbumContentIntent.OnDeleteMenuClick -> {
                _uiAction.tryEmit(
                    AlbumContentUiAction.ShowAnswerDialog(
                        AnswerDialog.DialogData(
                            header = resourceManager.getString(R.string.delete_photos_header),
                            answer = resourceManager.getString(
                                R.string.delete_photos_from_album_answer_pattern,
                                selectedPhotosId.size
                            ),
                            positiveButtonText = resourceManager.getString(R.string.yes),
                            negativeButtonText = resourceManager.getString(R.string.cancel)
                        )
                    )
                )
            }

            is AlbumContentIntent.OnPhotoClick -> {
                //open photo
            }

            is AlbumContentIntent.OnSelectPhoto -> {
                if (!intent.isSelected)
                    selectedPhotosId.add(intent.id)
                else selectedPhotosId.remove(intent.id)

                photoList = photoList.map { photo ->
                    if (photo.id == intent.id) {
                        photo.copy(isSelected = !intent.isSelected)
                    } else photo
                }.toMutableList()

                _uiAction.tryEmit(
                    AlbumContentUiAction.SelectPhotos(
                        photoList,
                        isSelectionMode,
                        if (isSelectionMode)
                            resourceManager.getString(
                                R.string.selected_pattern,
                                selectedPhotosId.size
                            )
                        else albumName
                    )
                )
            }

            is AlbumContentIntent.OnAnswerHandle -> {
                if (intent.result) {
                    deletePhoto()
                }
            }

            is AlbumContentIntent.OnAddAlbumList -> {
                addPhotos(intent.ids)
            }

            AlbumContentIntent.OnAddPhotoToAlbumClick -> {
                _uiAction.tryEmit(
                    AlbumContentUiAction.OpenChoosePhotoFragment
                )
            }
        }
    }

    private fun parseArgs(albumId: Int, albumName: String) {
        this.albumId = albumId
        this.albumName = albumName
        _uiState.update {
            it.copy(
                toolbarTitle = albumName,
                isLoading = true,
                errorUi = null
            )
        }
        loadAlbumContent()
    }

    private fun loadAlbumContent() {
        viewModelScope.launchRequest(
            request = {
                albumRepository.getAlbumPhotos(albumId)
            },
            onLoading = { isLoading ->
                _uiState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            },
            onSuccess = { photos ->
                photoList = getPhotoUiUseCase(photos.list)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        photoList = photoList
                    )
                }
            },
            onError = {
                _uiState.update {
                    it.copy(
                        errorUi = EmptyUi(
                            header = R.string.album_content_loading_error_header,
                            subtitle = R.string.album_content_loading_error_subtitle,
                            buttonText = R.string.refresh,
                            onButtonClick = {
                                loadAlbumContent()
                            }
                        )
                    )
                }
            }
        )
    }

    private fun deletePhoto() = viewModelScope.launchRequest(
        request = {
            albumRepository.removeFromAlbum(
                EditAlbumPhotosRequest(
                    albumId,
                    selectedPhotosId
                )
            )
        },
        onSuccess = {
            selectedPhotosId.clear()
            _uiState.update {
                it.copy(
                    isSelectionMode = isSelectionMode
                )
            }
            loadAlbumContent()
            showSuccessBanner(resourceManager.getString(R.string.photos_delete_success))
        },
        onError = {
            selectedPhotosId.clear()
            _uiState.update {
                it.copy(
                    isSelectionMode = isSelectionMode
                )
            }
            showErrorBanner(
                resourceManager.getString(R.string.photos_delete_error)
            )
        }
    )

    private fun addPhotos(ids: List<Int>) = viewModelScope.launchRequest(
        request = {
            albumRepository.addToAlbum(
                EditAlbumPhotosRequest(
                    albumId,
                    ids
                )
            )
        },
        onSuccess = {
            showSuccessBanner(resourceManager.getString(R.string.add_to_album_success))
            loadAlbumContent()
        },
        onError = {
            showErrorBanner(
                resourceManager.getString(R.string.add_to_album_error)
            )
        }
    )
}