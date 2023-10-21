package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.album.EditAlbumPhotosRequest
import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.AlbumPhoto
import com.example.picturegallery.domain.model.album.CreateAlbumRequest
import com.example.picturegallery.domain.model.album.UpdateAlbumRequest

interface AlbumRepository {
    suspend fun getAlbumList(): List<Album>
    suspend fun deleteAlbum(id: Int)

    suspend fun getAlbumPhotos(albumId: Int, offset: Int, count: Int): AlbumPhoto

    suspend fun createAlbum(createAlbumRequest: CreateAlbumRequest)

    suspend fun addToAlbum(addToAlbumRequest: EditAlbumPhotosRequest)

    suspend fun removeFromAlbum(removeFromAlbumRequest: EditAlbumPhotosRequest)

    suspend fun updateAlbum(updateAlbumRequest: UpdateAlbumRequest)
}