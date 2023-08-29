package com.example.picturegallery.domain.useCase

import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    suspend operator fun invoke(): List<PhotosAdapterUiState> {
        val resultList = mutableListOf<PhotosAdapterUiState>()
        photosRepository.getPhotos().list.forEach { photoCover ->
            resultList.add(
                PhotosAdapterUiState(
                    picture = PhotosAdapterUiState.getPictureThumb(photoCover.thumbnail)
                )
            )
        }
        return resultList
    }
}