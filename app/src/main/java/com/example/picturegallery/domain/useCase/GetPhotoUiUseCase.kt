package com.example.picturegallery.domain.useCase

import com.example.picturegallery.domain.model.album.CoverPhoto
import com.example.picturegallery.feature.photos.PhotosAdapterUiState
import com.example.picturegallery.utils.PictureUtils
import javax.inject.Inject

class GetPhotoUiUseCase @Inject constructor(
) {
    operator fun invoke(photos: List<CoverPhoto>): List<PhotosAdapterUiState> {
        return photos.map {
            PhotosAdapterUiState(
                id = it.id,
                picture = PictureUtils.getPhotoBitmap(it.thumbnail)
            )
        }
    }
}