package com.example.picturegallery.data.data_source

import com.example.picturegallery.domain.model.album.EditAlbumPhotosRequest
import com.example.picturegallery.domain.model.album.Album
import com.example.picturegallery.domain.model.album.AlbumPhoto
import com.example.picturegallery.domain.model.album.CreateAlbumRequest
import com.example.picturegallery.domain.model.album.UpdateAlbumRequest
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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
    suspend fun deleteAlbum(@Query("albumId") id: Int)

    @GET("$albumUrl/getAlbum")
    suspend fun getAlbumPhotos(@Query("albumId") id: Int, @Query("skip") offset: Int, @Query("take") count: Int): AlbumPhoto

    @POST("$albumUrl/createAlbum")
    suspend fun createAlbum(@Body createRequest: CreateAlbumRequest)

    @POST("$albumUrl/addToAlbum")
    suspend fun addToAlbum(@Body addToAlbumRequest: EditAlbumPhotosRequest)

    @POST("$albumUrl/removeFromAlbum")
    suspend fun removeFromAlbum(@Body removeFromAlbumRequest: EditAlbumPhotosRequest)

    @POST("$albumUrl/updateAlbum")
    suspend fun updateAlbum(@Body updateAlbumRequest: UpdateAlbumRequest)

}