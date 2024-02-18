package com.example.picturegallery.feature.photos

import android.graphics.Bitmap

data class PhotosAdapterUiState(
    val id: Int = -1,
    val picture: Bitmap? = null,
    val isSelected: Boolean = false
)
