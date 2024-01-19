package com.example.picturegallery.domain.useCase.impl

import com.example.picturegallery.domain.useCase.DateUseCase
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class DateUseCaseImpl @Inject constructor() : DateUseCase {
    override fun getFormattedDate(pattern: String, ms: Long): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(ms)
}