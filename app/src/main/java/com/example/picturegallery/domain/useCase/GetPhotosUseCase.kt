package com.example.picturegallery.domain.useCase

import com.example.picturegallery.domain.manager.ResourceManager
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.feature.photos.uistate.PhotosAdapterUiState
import com.example.picturegallery.utils.PictureUtils
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val dateUseCase: DateUseCase,
    private val resourceManager: ResourceManager
) {
    suspend operator fun invoke(skip: Int, photoCount: Int): List<PhotosAdapterUiState> {
        val resultList = mutableListOf<PhotosAdapterUiState>()
        photosRepository.getPhotos(skip, photoCount).list.forEach { (id, _, thumbnail) ->
            resultList.add(
                PhotosAdapterUiState(
                    id = id,
                    picture = PictureUtils.getPhotoBitmap(thumbnail)
                )
            )
        }
        return resultList
    }
}