package com.example.picturegallery.data.repository

import com.example.picturegallery.data.data_source.AlbumRemoteDataSource
import com.example.picturegallery.domain.model.album.EditAlbumPhotosRequest
import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.AlbumPhoto
import com.example.picturegallery.domain.model.album.CreateAlbumRequest
import com.example.picturegallery.domain.model.album.UpdateAlbumRequest
import com.example.picturegallery.domain.model.album.UpdateCoverRequest
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.domain.repository.AlbumRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumRemoteDataSource: AlbumRemoteDataSource,
    private val appDispatchers: AppDispatchers
) : AlbumRepository {
    override suspend fun getAlbumList(): List<Album> =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.getAlbumList()
        }

    override suspend fun deleteAlbum(id: Int) =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.deleteAlbum(id)
        }

    override suspend fun getAlbumPhotos(albumId: Int): AlbumPhoto =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.getAlbumPhotos(albumId)
        }

    override suspend fun createAlbum(createAlbumRequest: CreateAlbumRequest) =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.createAlbum(createAlbumRequest)
        }

    override suspend fun addToAlbum(addToAlbumRequest: EditAlbumPhotosRequest) =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.addToAlbum(addToAlbumRequest)
        }

    override suspend fun removeFromAlbum(removeFromAlbumRequest: EditAlbumPhotosRequest) =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.removeFromAlbum(removeFromAlbumRequest)
        }

    override suspend fun updateAlbum(updateAlbumRequest: UpdateAlbumRequest) =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.updateAlbum(updateAlbumRequest)
        }

    override suspend fun updateAlbumCover(request: UpdateCoverRequest) =
        withContext(appDispatchers.io) {
            albumRemoteDataSource.updateAlbumCover(request)
        }
}