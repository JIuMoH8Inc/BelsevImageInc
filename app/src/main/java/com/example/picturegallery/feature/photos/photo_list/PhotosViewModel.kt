package com.example.picturegallery.feature.photos.photo_list

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.useCase.GetPhotosUseCase
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOADING_ITEMS_COUNT = 42
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(
        PhotosUiState(
            toolbarTitle = resourceManager.getString(R.string.photo_toolbar_title)
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<PhotoUiAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var tempPhotoList: MutableList<PhotosAdapterUiState> = mutableListOf()

    fun handleIntent(intent: PhotoFragmentIntent) {
        when (intent) {
            is PhotoFragmentIntent.OnLoadPhotoList -> {
                load(intent.isInitLoading, intent.offset)
            }

            is PhotoFragmentIntent.OnPhotoClick -> {
                _uiAction.tryEmit(
                    PhotoUiAction.OpenViewPhotoFragment(intent.id)
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
                            isLastPage = photos.size < LOADING_ITEMS_COUNT
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