package com.example.picturegallery.feature.albums.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.picturegallery.R
import com.example.picturegallery.domain.manager.ResourceManager
import com.example.picturegallery.domain.useCase.GetAlbumUseCase
import com.example.picturegallery.feature.albums.action.AlbumsFragmentAction
import com.example.picturegallery.feature.albums.intent.AlbumsFragmentIntent
import com.example.picturegallery.feature.albums.uistate.AlbumsFragmentUiState
import com.example.picturegallery.ui.fragment.base.BaseViewModel
import com.example.picturegallery.utils.flow.SingleFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        AlbumsFragmentUiState(toolbarTitle = resourceManager.getString(R.string.album_toolbar_title))
    )
    val uiState = _uiState.asStateFlow()

    private val _uiAction = SingleFlow<AlbumsFragmentAction>()
    val uiAction = _uiAction.asSharedFlow()

    private fun load() {
        _uiState.update {
            it.copy(
                isLoading = true,
                isError = false
            )
        }
        viewModelScope.launch {
            kotlin.runCatching { getAlbumUseCase() }
                .onSuccess { albums ->
                    _uiState.update {
                        it.copy(
                            albumList = albums,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false
                        )
                    }
                }
        }
    }

    fun handleIntent(intent: AlbumsFragmentIntent) {
        when (intent) {
            is AlbumsFragmentIntent.OnAlbumClick -> {
                _uiAction.tryEmit(
                    AlbumsFragmentAction.OpenAlbum(intent.id)
                )
            }

            is AlbumsFragmentIntent.OnMoreClick -> {
                _uiAction.tryEmit(
                    AlbumsFragmentAction.OpenAlbumDetailsBottomSheet(intent.album)
                )
            }

            AlbumsFragmentIntent.OnLoadAlbums -> load()
        }
    }
}