package com.example.picturegallery.data.repository

import com.example.picturegallery.data.data_source.UserRemoteDataSource
import com.example.picturegallery.domain.model.dispatchers.AppDispatchers
import com.example.picturegallery.domain.model.signin.SignInRequest
import com.example.picturegallery.domain.repository.UserRepository
import kotlinx.coroutines.withContext
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