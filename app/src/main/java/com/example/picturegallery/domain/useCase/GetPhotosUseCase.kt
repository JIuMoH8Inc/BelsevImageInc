package com.example.picturegallery.domain.useCase

import com.example.picturegallery.R
import com.example.picturegallery.domain.manager.ResourceManager
import com.example.picturegallery.domain.repository.PhotosRepository
import com.example.picturegallery.feature.photos.photo_list.PhotosAdapterUiState
import com.example.picturegallery.utils.PictureUtils
import javax.inject.Inject

private const val DATE_PATTERN = "dd.MM.yyyy"

class GetPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val dateUseCase: DateUseCase,
    private val resourceManager: ResourceManager
) {
    suspend operator fun invoke(skip: Int, photoCount: Int): List<PhotosAdapterUiState> {
        val resultList = mutableListOf<PhotosAdapterUiState>()
        photosRepository.getPhotos(skip, photoCount).list.forEach { photoCover ->
            resultList.add(
                PhotosAdapterUiState(
                    id = photoCover.id,
                    picture = PictureUtils.getPhotoBitmap(photoCover.thumbnail),
                    title = photoCover.fileName,
                    creationDate = photoCover.creationTime?.let {  date ->
                        dateUseCase.getFormattedDate(DATE_PATTERN, date.time)
                    } ?: run {
                        resourceManager.getString(R.string.unknown)
                    }
                )
            )
        }
        return resultList
    }
}