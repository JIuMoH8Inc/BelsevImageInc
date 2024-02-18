package com.example.picturegallery.data.data_source

import com.example.picturegallery.domain.model.signin.SignInRequest
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRemoteDataSource {

    @POST("$userUrl/getJwtToken")
    suspend fun signIn(@Body request: SignInRequest): String

    companion object {

        private const val userUrl = "user"

        @JvmStatic
        fun create(retrofit: Retrofit): UserRemoteDataSource =
            retrofit.create(UserRemoteDataSource::class.java)

    }

}