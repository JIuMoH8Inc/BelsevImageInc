package com.example.picturegallery.feature.photos.photo_view

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.picturegallery.R

data class ViewPhotoUiState(
    @StringRes val toolbarTitle: Int = R.string.view_photo,
    val photo: Bitmap? = null,
    val isError: Boolean = false,
    @StringRes val photoErrorHeader: Int = R.string.view_photo_load_error_header,
    @StringRes val errorButtonText: Int = R.string.try_again_text,
    @DrawableRes val errorDrawable: Int = R.drawable.ic_photo,
    val isNeedToRotate: Boolean = false
)