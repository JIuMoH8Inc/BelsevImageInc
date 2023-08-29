package com.example.picturegallery.domain.model.photos

import com.example.picturegallery.domain.model.album.CoverPhoto

data class PhotoResponse(
    val itemCount: Int = 0,
    val list: List<CoverPhoto> = emptyList()
)
