package com.example.picturegallery.data.repository

import com.example.picturegallery.data.data_source.PhotosRemoteDataSource
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.domain.model.photos.PhotoResponse
import com.example.picturegallery.domain.repository.PhotosRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val photosRemoteDataSource: PhotosRemoteDataSource,
    private val appDispatchers: AppDispatchers
) : PhotosRepository {
    override suspend fun getPhotos(skip: Int, take: Int): PhotoResponse =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.getPhotos(skip, take)
        }
}