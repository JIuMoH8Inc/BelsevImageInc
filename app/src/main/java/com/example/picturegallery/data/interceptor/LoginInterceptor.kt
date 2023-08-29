package com.example.picturegallery.data.interceptor

import com.example.picturegallery.domain.exception.InvalidLoginPasswordException
import okhttp3.Response

class LoginInterceptor : NetworkInterceptor() {
    override fun getResponse(result: Response): Response =
        when (result.code) {
            400 -> throw InvalidLoginPasswordException()
            else -> super.getResponse(result)
        }
}