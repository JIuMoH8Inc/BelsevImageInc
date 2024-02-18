package com.example.picturegallery.feature.create_album

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.model.album.CreateAlbumRequest
import com.example.picturegallery.domain.repository.AlbumRepository
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.domain.useCase.GetPhotoUiUseCase
import com.example.picturegallery.feature.photos.PhotosAdapterUiState
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.ui.views.EmptyUi
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateAlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val photosRepository: PhotosRepository,
    private val getPhotoUiUseCase: GetPhotoUiUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(
        CreateAlbumUiState(
            toolbarTitle = resourceManager.getString(R.string.create_album_toolbar)
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<CreateAlbumUiAction>()
    val uiAction = _uiAction.asSharedFlow()
    private val selectedPhotos = mutableListOf<Int>()
    private var photoList = emptyList<PhotosAdapterUiState>()

    private var createAlbumRequest = CreateAlbumRequest()
    private val isEmptyName: Boolean
        get() = createAlbumRequest.name.isEmpty()

    private val isPhotoListEmpty: Boolean
        get() = selectedPhotos.isEmpty()

    fun handleIntent(intent: CreateAlbumIntent) {
        when (intent) {
            CreateAlbumIntent.LoadPhotos -> {
                load()
            }

            is CreateAlbumIntent.OnSelectPhoto -> {
                if (!intent.isSelected)
                    selectedPhotos.add(intent.id)
                else selectedPhotos.remove(intent.id)

                createAlbumRequest = createAlbumRequest.copy(
                    albumPhotoIds = selectedPhotos
                )

                photoList = photoList.map { photo ->
                    if (photo.id == intent.id) {
                        photo.copy(isSelected = !intent.isSelected)
                    } else photo
                }.toMutableList()

                _uiAction.tryEmit(
                    CreateAlbumUiAction.SelectPhotos(photoList)
                )
                _uiState.update {
                    it.copy(
                        isEmptyPhotoList = isPhotoListEmpty
                    )
                }
            }

            is CreateAlbumIntent.OnDescChange -> {
                createAlbumRequest = createAlbumRequest.copy(
                    description = intent.desc
                )
            }

            is CreateAlbumIntent.OnTitleChange -> {
                createAlbumRequest = createAlbumRequest.copy(
                    name = intent.title
                )

                _uiState.update {
                    it.copy(
                        emptyNameError =
                        if (intent.title.isEmpty())
                            resourceManager.getString(R.string.create_album_empty_name_error)
                        else null
                    )
                }

            }

            CreateAlbumIntent.OnCreateAlbumClick -> {
                if (isEmptyName) {
                    _uiAction.tryEmit(
                        CreateAlbumUiAction.SetEmptyNameError(
                            resourceManager.getString(R.string.create_album_empty_name_error)
                        )
                    )
                } else {
                    _uiAction.tryEmit(
                        CreateAlbumUiAction.SetEmptyNameError(null)
                    )

                    if (isPhotoListEmpty) {
                        _uiState.update {
                            it.copy(
                                isEmptyPhotoList = isPhotoListEmpty
                            )
                        }
                    }
                }

                if (!isPhotoListEmpty && !isEmptyName) {
                    createAlbumRequest = createAlbumRequest.copy(
                        coverPhotoId = selectedPhotos.first()
                    )
                    createAlbum()
                }
            }

            is CreateAlbumIntent.ParseArgs -> {
                intent.photoList?.let {
                    selectedPhotos.addAll(intent.photoList.toMutableList())
                    createAlbumRequest =
                        createAlbumRequest.copy(albumPhotoIds = selectedPhotos)

                    _uiState.update {
                        it.copy(
                            isPhotoHeaderVisible = false
                        )
                    }
                } ?: run {
                    load()
                }
            }
        }
    }

    private fun load() = viewModelScope.launchRequest(
        request = {
            photosRepository.getPhotosWithoutPaging()
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
                    photoList = photoList
                )
            }
        },
        onError = {
            _uiState.update {
                it.copy(
                    errorUi = EmptyUi(
                        header = R.string.create_album_loading_photos_error_header,
                        subtitle = R.string.create_album_loading_photos_subtitle,
                        buttonText = R.string.create_album_loading_photos_button_text
                    ) {
                        handleIntent(CreateAlbumIntent.LoadPhotos)
                    }
                )
            }
        }
    )

    private fun createAlbum() = viewModelScope.launchRequest(
        request = {
            albumRepository.createAlbum(createAlbumRequest)
        },
        onSuccess = {
            _uiAction.tryEmit(
                CreateAlbumUiAction.OpenNewAlbum(it, createAlbumRequest.name)
            )
            showSuccessBanner(resourceManager.getString(R.string.create_album_success))
        },
        onError = {
            showErrorBanner(resourceManager.getString(R.string.create_album_error))
        }
    )
}