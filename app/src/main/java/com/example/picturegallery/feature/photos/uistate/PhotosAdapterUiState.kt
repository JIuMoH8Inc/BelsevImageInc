package com.example.picturegallery.feature.photos.uistate

import android.graphics.Bitmap
import com.example.picturegallery.utils.PictureUtils

data class PhotosAdapterUiState(
    val picture: Bitmap? = null
) {
    companion object {
        fun getPictureThumb(base64: String): Bitmap = PictureUtils.getPhotoBitmap(base64)
    }
}
