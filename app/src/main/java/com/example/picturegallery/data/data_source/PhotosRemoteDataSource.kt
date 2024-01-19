package com.example.picturegallery.data.data_source

import com.example.picturegallery.domain.model.photos.PhotoResponse
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosRemoteDataSource {

    companion object {
        private const val photosUrl = "photos"

        fun create(retrofit: Retrofit): PhotosRemoteDataSource =
            retrofit.create(PhotosRemoteDataSource::class.java)
    }

    @GET("$photosUrl/getPhotos")
    suspend fun getPhotos(@Query("skip") skip: Int? = 0, @Query("take") take: Int? = 20): PhotoResponse

    @DELETE("$photosUrl/deletePhotos")
    suspend fun deletePhotos(@Body photoIdList: List<Int>)

    @GET("$photosUrl/getPhotoFile")
    suspend fun downloadPhoto(@Query("photoId") id: Int)

}