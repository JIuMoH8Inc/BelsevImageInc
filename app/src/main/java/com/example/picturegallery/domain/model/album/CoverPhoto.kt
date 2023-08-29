package com.example.picturegallery.domain.model.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class CoverPhoto(
    val id: Int = 0,
    val fileName: String = "",
    val thumbnail: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val latitude: Int? = 0,
    val longitude: Int? = 0,
    val thumbnailWidth: Int = 0,
    val creationTime: String = "",
    val ownerName: String? = "",
    val ownerId: String = "",
    val fileSize: Int = 0,
    val isFavorite: Boolean = false,
    val description: String = "",
    val objects: String = ""
) : Parcelable
