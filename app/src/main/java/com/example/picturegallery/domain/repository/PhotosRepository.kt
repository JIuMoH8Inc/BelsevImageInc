package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.photos.PhotoResponse

interface PhotosRepository {
    suspend fun getPhotos(): PhotoResponse
}