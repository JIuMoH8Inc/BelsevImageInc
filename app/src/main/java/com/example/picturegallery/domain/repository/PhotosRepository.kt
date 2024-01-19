package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.photos.PhotoResponse
import com.example.picturegallery.domain.model.photos.UploadPhotoRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface PhotosRepository {
    suspend fun getPhotos(skip: Int, take: Int): PhotoResponse

    suspend fun getPhotosWithoutPaging(): PhotoResponse

    suspend fun deletePhotos(photoIdList: List<Int>)
    suspend fun getPhotoFile(id: Int): ResponseBody

    suspend fun uploadPhotos(albumId: Int? = null, files: List<MultipartBody.Part>)

    suspend fun uploadPhoto(photo: UploadPhotoRequest)
}