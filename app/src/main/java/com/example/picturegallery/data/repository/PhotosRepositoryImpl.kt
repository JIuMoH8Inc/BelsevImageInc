package com.example.picturegallery.data.repository

import com.example.picturegallery.data.data_source.PhotosRemoteDataSource
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.domain.model.photos.PhotoResponse
import com.example.picturegallery.domain.model.photos.UploadPhotoRequest
import com.example.picturegallery.domain.repository.PhotosRepository
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val photosRemoteDataSource: PhotosRemoteDataSource,
    private val appDispatchers: AppDispatchers
) : PhotosRepository {
    override suspend fun getPhotos(skip: Int, take: Int): PhotoResponse =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.getPhotos(skip, take)
        }

    override suspend fun getPhotosWithoutPaging() =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.getPhotos()
        }

    override suspend fun deletePhotos(photoIdList: List<Int>) =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.deletePhotos(photoIdList)
        }

    override suspend fun downloadFile(id: Int) =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.downloadPhoto(id)
        }

    override suspend fun uploadPhotos(albumId: Int?, files: List<MultipartBody.Part>) =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.uploadFiles(
                albumId = albumId,
                files = files
            )
        }

    override suspend fun uploadPhoto(photo: UploadPhotoRequest) =
        withContext(appDispatchers.io) {
            photosRemoteDataSource.uploadPhoto(photo)
        }
}