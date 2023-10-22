package com.example.picturegallery.domain.useCase

interface DateUseCase {
    fun getFormattedDate(pattern: String, ms: Long): String
}