package com.example.picturegallery.data.interceptor

import com.example.picturegallery.domain.exception.InvalidLoginPasswordException
import com.example.picturegallery.domain.exception.NoInternetException
import com.example.picturegallery.domain.exception.UnknownErrorException
import okhttp3.Interceptor
import okhttp3.Response

open class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder().build()
        val result: Response
        try {
            result = chain.proceed(builder)
        } catch (e: Exception) {
            throw NoInternetException()
        }

        return getResponse(result)
    }

    open fun getResponse(result: Response): Response =
        when (result.code) {
            200, 201 -> result
            400 -> throw UnknownErrorException()
            else -> result
        }
}