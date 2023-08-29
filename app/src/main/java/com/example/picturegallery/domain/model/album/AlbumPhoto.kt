package com.example.picturegallery.domain.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumPhoto (
    val totalCount: Int = 0,
    val list: List<CoverPhoto> = emptyList(),
    val albumName: String = "",
    val albumDescription: String = ""
) : Parcelable
