package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.photos.PhotoResponse
import okhttp3.ResponseBody

interface PhotosRepository {
    suspend fun getPhotos(skip: Int, take: Int): PhotoResponse
    suspend fun getPhotoFile(id: Int): ResponseBody
    suspend fun deletePhotos(photoIdList: List<Int>)
}