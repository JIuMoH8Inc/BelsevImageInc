package com.example.picturegallery.feature.photos.photo_view

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.picturegallery.R
import com.example.picturegallery.ui.views.EmptyUi

data class ViewPhotoUiState(
    @StringRes val toolbarTitle: Int = R.string.view_photo,
    val photo: Bitmap? = null,
    @StringRes val photoErrorHeader: Int = R.string.view_photo_load_error_header,
    @DrawableRes val errorDrawable: Int = R.drawable.ic_photo,
    val isNeedToRotate: Boolean = false,
    val errorUi: EmptyUi? = null
)