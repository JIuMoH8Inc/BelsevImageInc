package com.example.picturegallery.domain.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditAlbumPhotosRequest(
    val albumId: Int = 0,
    val albumPhotoIds: List<Int> = emptyList()
) : Parcelable
