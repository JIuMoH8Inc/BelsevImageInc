package com.example.picturegallery.feature.photos.uistate

import android.graphics.Bitmap
import androidx.annotation.MenuRes
import com.example.picturegallery.R

data class PhotosAdapterUiState(
    val id: Int = -1,
    val picture: Bitmap? = null,
    val isSelected: Boolean = false,
    @MenuRes val menu: Int = R.menu.photo_context_menu
)
