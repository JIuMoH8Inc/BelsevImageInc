package com.example.picturegallery.domain.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Token(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val token: String? = ""
) {
    constructor(token: String): this(0, token)
}
