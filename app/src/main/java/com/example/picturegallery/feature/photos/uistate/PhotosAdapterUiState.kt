package com.example.picturegallery.feature.photos.uistate

import android.graphics.Bitmap

data class PhotosAdapterUiState(
    val id: Int = 0,
    val picture: Bitmap? = null,
    val title: String = "",
    val creationDate: String = ""
)
