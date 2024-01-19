package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.photos.PhotoResponse
import com.example.picturegallery.domain.model.photos.UploadPhotoRequest
import okhttp3.MultipartBody

interface PhotosRepository {
    suspend fun getPhotos(skip: Int, take: Int): PhotoResponse

    suspend fun uploadPhotos(albumId: Int? = null, files: List<MultipartBody.Part>)

    suspend fun uploadPhoto(photo: UploadPhotoRequest)

    suspend fun getPhotosWithoutPaging(): PhotoResponse

    suspend fun deletePhotos(photoIdList: List<Int>)

    suspend fun downloadFile(id: Int)
}