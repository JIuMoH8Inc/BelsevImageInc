package com.example.picturegallery.feature.photos

import android.app.Application
import android.util.Base64
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.manager.FileManager
import com.example.picturegallery.domain.model.photos.UploadPhotoRequest
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.domain.useCase.GetPhotoUiUseCase
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.extensions.getByteArray
import com.example.picturegallery.utils.extensions.getFileName
import com.example.picturegallery.utils.extensions.launchRequest
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOADING_ITEMS_COUNT = 42

class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotoUiUseCase,
    private val photosRepository: PhotosRepository,
    private val fileManager: FileManager,
    private val application: Application
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(
        PhotosUiState(
            toolbarTitle = resourceManager.getString(R.string.photo_toolbar_title)
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<PhotosUiAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var tempPhotoList: MutableList<PhotosAdapterUiState> = mutableListOf()
    private var selectedPhotosId = mutableListOf<Int>()
    private var isAddToAlbumMode = false
    private val isSelectionMode: Boolean
        get() = selectedPhotosId.isNotEmpty()

    fun handleIntent(intent: PhotoFragmentIntent) {
        when (intent) {
            is PhotoFragmentIntent.OnLoadPhotoList -> {
                load(intent.isInitLoading, intent.offset)
            }

            is PhotoFragmentIntent.OnPhotoClick -> {
                _uiAction.tryEmit(
                    PhotosUiAction.OpenViewPhotoFragment(intent.id)
                )
            }

            is PhotoFragmentIntent.OnSelectPhoto -> {
                if (!intent.isSelected)
                    selectedPhotosId.add(intent.id)
                else selectedPhotosId.remove(intent.id)

                tempPhotoList = tempPhotoList.map { photo ->
                    if (photo.id == intent.id) {
                        photo.copy(isSelected = !intent.isSelected)
                    } else photo
                }.toMutableList()

                _uiAction.tryEmit(
                    PhotosUiAction.SelectPhotos(
                        tempPhotoList,
                        isSelectionMode,
                        if (isSelectionMode)
                            resourceManager.getString(
                                R.string.selected_pattern,
                                selectedPhotosId.size
                            )
                        else {
                            resourceManager.getString(
                                if (!isAddToAlbumMode) {
                                    R.string.photo_toolbar_title
                                } else {
                                    R.string.add_to_album_toolbar
                                }
                            )
                        },
                        isAddToAlbumMode
                    )
                )
            }

            PhotoFragmentIntent.OnDeleteMenuClick -> {
                _uiAction.tryEmit(
                    PhotosUiAction.ShowAnswerDialog(
                        AnswerDialog.DialogData(
                            header = resourceManager.getString(R.string.delete_photos_header),
                            answer = resourceManager.getString(
                                R.string.delete_photos_answer_pattern,
                                selectedPhotosId.size
                            ),
                            positiveButtonText = resourceManager.getString(R.string.yes),
                            negativeButtonText = resourceManager.getString(R.string.cancel)
                        )
                    )
                )
            }

            is PhotoFragmentIntent.OnAnswerHandle -> {
                if (intent.result) {
                    deletePhoto()
                }
            }

            PhotoFragmentIntent.OnAddToAlbumClick -> {
                _uiAction.tryEmit(
                    PhotosUiAction.AddPhotosToAlbum(selectedPhotosId)
                )
            }

            is PhotoFragmentIntent.OnParseArgs -> {
                if (intent.isAddToAlbum) {
                    isAddToAlbumMode = true
                    _uiState.update {
                        it.copy(
                            isAddToAlbum = true,
                            toolbarTitle = resourceManager.getString(R.string.add_to_album_toolbar)
                        )
                    }
                    load(true, 0)
                } else load(true, 0)
            }

            PhotoFragmentIntent.OnToAlbumMenuClick -> {
                _uiAction.tryEmit(
                    PhotosUiAction.OpenChooseAlbumType(selectedPhotosId.toIntArray())
                )
            }

            is PhotoFragmentIntent.OnAddPhotoButtonClick -> {
                uploadPhoto(intent.registry)
            }
        }
    }

    //TODO придумать как измениять список без .map { copy() } для пагинации
    private fun load(isInitLoading: Boolean, offset: Int) {
        initLoad(isInitLoading)
        viewModelScope.launchRequest(
            request = {
                photosRepository.getPhotos(offset, LOADING_ITEMS_COUNT)
            },
            onLoading = { isLoading ->
                _uiState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            },
            onSuccess = { photos ->
                val photosUi = getPhotosUseCase(photos.list)
                tempPhotoList.addAll(photosUi)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        photoList = tempPhotoList.map { photo -> photo.copy() },
                        isNextPageLoading = false,
                        isLastPage = photosUi.size <= LOADING_ITEMS_COUNT
                    )
                }
            },
            onError = {
                _uiState.update {
                    it.copy(
                        isError = true,
                        isNextPageLoading = false
                    )
                }
            }
        )
    }

    private fun deletePhoto() = viewModelScope.launch {
        runCatching {
            photosRepository.deletePhotos(selectedPhotosId)
        }
            .onSuccess {
                selectedPhotosId.clear()
                _uiState.update {
                    it.copy(
                        isSelectionMode = isSelectionMode
                    )
                }
                load(false, 0)
                showSuccessBanner(
                    resourceManager.getString(R.string.photos_delete_success)
                )
            }
            .onFailure {
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
    }

    private fun uploadPhoto(registry: ActivityResultRegistry) {
        fileManager.openFilePicker(
            registry
        ) { images ->
            val handler = CoroutineExceptionHandler { _, _ ->
                showErrorBanner(
                    resourceManager.getString(R.string.upload_photo_error)
                )
            }
            viewModelScope.launch(handler) {
                val uploadPhotosRequest = images.map {
                    async {
                        photosRepository.uploadPhoto(
                            UploadPhotoRequest(
                                Base64.encodeToString(it.getByteArray(application), Base64.NO_WRAP),
                                it.getFileName(application) ?: "default.jpg"
                            )
                        )
                    }
                }
                handleUploadPhotoSuccess(uploadPhotosRequest.awaitAll().size)
            }
        }
    }

    private fun handleUploadPhotoSuccess(size: Int) {
        showSuccessBanner(
            resourceManager.getString(
                R.string.upload_photo_success,
                size
            )
        )
        load(true, 0)
    }

    private fun initLoad(isInitLoading: Boolean) {
        tempPhotoList.clear()

        _uiState.update {
            it.copy(
                isError = false,
                isEmpty = false,
                isNextPageLoading = !isInitLoading,
                photoList = tempPhotoList
            )
        }
    }
}