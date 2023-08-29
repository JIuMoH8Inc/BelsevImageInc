package com.example.picturegallery.data.repository

import com.example.picturegallery.data.data_source.AlbumRemoteDataSource
import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.AlbumPhoto
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.domain.repository.AlbumRepository
import io.reactivex.Completable
import io.reactivex.Single
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

    override fun deleteAlbum(id: Int): Completable {
        return albumRemoteDataSource.deleteAlbum(id)
    }
}