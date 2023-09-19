package com.example.picturegallery.domain.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateAlbumRequest(
    val albumId: Int = 0,
    val name: String = "",
    val description: String = "",
    val isPublic: Boolean = false
) : Parcelable
