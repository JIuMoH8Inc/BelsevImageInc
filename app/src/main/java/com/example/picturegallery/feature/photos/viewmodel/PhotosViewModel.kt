package com.example.picturegallery.feature.photos.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.domain.useCase.GetPhotosUseCase
import com.example.picturegallery.feature.photos.intent.PhotoFragmentIntent
import com.example.picturegallery.feature.photos.uiAction.PhotosUiAction
import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState
import com.example.picturegallery.feature.photos.uistate.PhotosUiState
import com.example.picturegallery.ui.dialog.AnswerDialog
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOADING_ITEMS_COUNT = 42

class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val photosRepository: PhotosRepository
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
    private val isSelectionMode: Boolean
        get() = selectedPhotosId.isNotEmpty()

    fun handleIntent(intent: PhotoFragmentIntent) {
        when (intent) {
            is PhotoFragmentIntent.OnLoadPhotoList -> {
                load(intent.isInitLoading, intent.offset)
            }

            is PhotoFragmentIntent.OnPhotoClick -> {
                //открытие фото на просмотр
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
                        else resourceManager.getString(R.string.photo_toolbar_title)
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
                            negativeButtonText = resourceManager.getString(R.string.cancel),
                            positiveButtonClick = {
                                deletePhoto()
                            }
                        )
                    )
                )
            }
        }
    }

    //TODO придумать как измениять список без .map { copy() } для пагинации
    private fun load(isInitLoading: Boolean, offset: Int) =
        viewModelScope.launch {

            initLoad(isInitLoading)

            kotlin.runCatching {
                getPhotosUseCase(offset, LOADING_ITEMS_COUNT)
            }
                .onSuccess { photos ->
                    tempPhotoList.addAll(photos)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            photoList = tempPhotoList.map { photo -> photo.copy() },
                            isNextPageLoading = false,
                            isLastPage = photos.size <= LOADING_ITEMS_COUNT
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            isNextPageLoading = false
                        )
                    }
                }
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
                _uiAction.tryEmit(
                    PhotosUiAction.ShowSuccessBanner(
                        resourceManager.getString(R.string.photos_delete_success)
                    )
                )
            }
            .onFailure {
                selectedPhotosId.clear()
                _uiState.update {
                    it.copy(
                        isSelectionMode = isSelectionMode
                    )
                }
                _uiAction.tryEmit(
                    PhotosUiAction.ShowErrorBanner(
                        resourceManager.getString(R.string.photos_delete_error)
                    )
                )
            }
    }

    private fun initLoad(isInitLoading: Boolean) {
        tempPhotoList.clear()

        _uiState.update {
            it.copy(
                isLoading = isInitLoading,
                isError = false,
                isEmpty = false,
                isNextPageLoading = !isInitLoading,
                photoList = tempPhotoList
            )
        }
    }
}