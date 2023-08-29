package com.example.picturegallery.data.data_source

import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.AlbumPhoto
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumRemoteDataSource {

    companion object {
        private const val albumUrl = "Albums"

        @JvmStatic
        fun create(retrofit: Retrofit): AlbumRemoteDataSource =
            retrofit.create(AlbumRemoteDataSource::class.java)

    }

    @GET("$albumUrl/GetAlbums")
    suspend fun getAlbumList(): List<Album>

    @DELETE("$albumUrl/deleteAlbum")
    fun deleteAlbum(@Query("albumId") id: Int): Completable

}