package com.example.picturegallery.data.manager

import androidx.exifinterface.media.ExifInterface
import com.example.picturegallery.domain.manager.FileManager
import com.example.picturegallery.domain.manager.ImageManager
import javax.inject.Inject

private const val ORIENTATION_NEED_ROTATE = "6"
class ImageManagerImpl @Inject constructor(
    private val fileManager: FileManager
) : ImageManager {
    override fun isNeedToRotate(bytes: ByteArray): Boolean {
        val filePath = fileManager.createTempFile(bytes)
        val exif = ExifInterface(filePath)
        return exif.getAttribute(ExifInterface.TAG_ORIENTATION) == ORIENTATION_NEED_ROTATE
    }
}