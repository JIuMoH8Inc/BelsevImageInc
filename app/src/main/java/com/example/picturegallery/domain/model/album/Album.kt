package com.example.picturegallery.domain.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val coverPhoto: CoverPhoto = CoverPhoto(),
    val itemsCount: Int = 0
) : Parcelable
