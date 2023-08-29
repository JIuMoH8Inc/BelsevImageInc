package com.example.picturegallery.feature.albums.uistate

import android.graphics.Bitmap
import android.os.Parcelable
import com.example.picturegallery.utils.PictureUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumAdapterUiState(
    val id: Int = 0,
    val itemCount: Int = 0,
    val albumName: String = "",
    val albumThumb: Bitmap? = null,
    val albumDescription: String = ""
) : Parcelable {
    companion object {
        fun getAlbumThumb(base64: String): Bitmap =
            PictureUtils.getPhotoBitmap(base64)
    }
}
