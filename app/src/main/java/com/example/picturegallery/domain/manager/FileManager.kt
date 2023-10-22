package com.example.picturegallery.domain.manager

interface FileManager {
    fun createTempFile(bytes: ByteArray): String
}