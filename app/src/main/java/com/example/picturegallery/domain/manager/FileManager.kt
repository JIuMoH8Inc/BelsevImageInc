package com.example.picturegallery.domain.manager

import android.net.Uri
import androidx.activity.result.ActivityResultRegistry

interface FileManager {
    fun createTempFile(bytes: ByteArray): String
    fun openFilePicker(
        registry: ActivityResultRegistry,
        callback: (List<Uri>) -> Unit
    )
}