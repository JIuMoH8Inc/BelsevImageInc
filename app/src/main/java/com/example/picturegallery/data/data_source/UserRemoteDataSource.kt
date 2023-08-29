package com.example.picturegallery.data.data_source

import android.content.Context
import androidx.room.Room
import com.example.picturegallery.domain.model.room.db.LocalDB
import com.example.picturegallery.domain.model.signin.SignInRequest
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
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