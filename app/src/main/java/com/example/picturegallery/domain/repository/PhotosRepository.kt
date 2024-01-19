package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.photos.PhotoResponse

interface PhotosRepository {
    suspend fun getPhotos(skip: Int, take: Int): PhotoResponse

    suspend fun deletePhotos(photoIdList: List<Int>)

    suspend fun downloadFile(id: Int)
}