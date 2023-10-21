package com.example.picturegallery.domain.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateAlbumRequest(
    val name: String = "",
    val coverPhotoId: Int = 0,
    val description: String = "",
    val albumPhotoIds: List<Int> = emptyList()
) : Parcelable
