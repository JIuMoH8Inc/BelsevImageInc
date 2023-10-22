package com.example.picturegallery.domain.manager

interface ImageManager {
    fun isNeedToRotate(bytes: ByteArray): Boolean
}