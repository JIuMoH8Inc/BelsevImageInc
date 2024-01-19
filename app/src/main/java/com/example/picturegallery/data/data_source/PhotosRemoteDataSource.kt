package com.example.picturegallery.data.data_source

import com.example.picturegallery.domain.model.photos.PhotoResponse
import com.example.picturegallery.domain.model.photos.UploadPhotoRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.HTTP
import retrofit2.http.Query

interface PhotosRemoteDataSource {

    companion object {
        private const val photosUrl = "photos"

        fun create(retrofit: Retrofit): PhotosRemoteDataSource =
            retrofit.create(PhotosRemoteDataSource::class.java)
    }

    @GET("$photosUrl/getPhotos")
    suspend fun getPhotos(@Query("skip") skip: Int = 0, @Query("take") take: Int = 100000): PhotoResponse

    @HTTP(method = "DELETE", path = "$photosUrl/deletePhotos", hasBody = true)
    suspend fun deletePhotos(@Body photoIdList: List<Int>)

    @GET("$photosUrl/getPhotoFile")
    suspend fun downloadPhoto(@Query("photoId") id: Int)

    @Multipart
    @POST("$photosUrl/uploadPhotos")
    suspend fun uploadFiles(@Query("albumId") albumId: Int? = null, @Part files: List<MultipartBody.Part>)

    @POST("$photosUrl/uploadPhoto")
    suspend fun uploadPhoto(@Body photo: UploadPhotoRequest)

}