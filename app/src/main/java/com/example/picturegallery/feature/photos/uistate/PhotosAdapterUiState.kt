package com.example.picturegallery.feature.photos.uistate

import android.graphics.Bitmap

data class PhotosAdapterUiState(
    val picture: Bitmap? = null,
    val title: String = "",
    val creationDate: String = ""
)
