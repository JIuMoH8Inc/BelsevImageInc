package com.example.picturegallery.feature.albums

import android.graphics.Bitmap
import android.os.Parcelable
import com.example.picturegallery.utils.PictureUtils
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumAdapterUiState(
    val id: Int = 0,
    val itemCount: String = "",
    val albumName: String = "",
    val albumThumb: Bitmap? = null,
    val albumDesc: String = ""
) : Parcelable {
    companion object {
        fun getAlbumThumb(base64: String): Bitmap =
            PictureUtils.getPhotoBitmap(base64)
    }
}
