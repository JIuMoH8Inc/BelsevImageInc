package com.example.picturegallery.domain.model.photos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadPhotoRequest(
    val file: String = "",
    val fileName: String = "",
    val albumId: Int? = null
) : Parcelable
