package com.example.picturegallery.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

interface UserRepository {
    suspend fun signIn(login: String, password: String): String
}