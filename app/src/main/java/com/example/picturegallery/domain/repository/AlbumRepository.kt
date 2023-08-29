package com.example.picturegallery.domain.repository

import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.AlbumPhoto
import io.reactivex.Completable
import io.reactivex.Single

interface AlbumRepository {
    suspend fun getAlbumList(): List<Album>
    fun deleteAlbum(id: Int): Completable
}