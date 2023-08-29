package com.example.picturegallery.data.repository

import com.example.picturegallery.data.data_source.UserRemoteDataSource
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.domain.model.room.db.LocalDB
import com.example.picturegallery.domain.model.signin.SignInRequest
import com.example.picturegallery.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val appDispatchers: AppDispatchers
) : UserRepository {

    override suspend fun signIn(login: String, password: String): String =
        withContext(appDispatchers.io) {
            userRemoteDataSource.signIn(
                SignInRequest(
                    userName = login,
                    password = password
                )
            )
        }
}