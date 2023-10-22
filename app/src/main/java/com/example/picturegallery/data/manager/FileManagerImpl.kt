package com.example.picturegallery.data.manager

import android.app.Application
import android.os.Environment
import com.example.picturegallery.domain.manager.FileManager
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FileManagerImpl @Inject constructor(
    private val context: Application
) : FileManager {
    override fun createTempFile(bytes: ByteArray): String {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempImage = File.createTempFile(
            "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}", ".jpeg", storageDir
        )
        val fos = FileOutputStream(tempImage)
        fos.write(bytes)
        fos.close()
        tempImage.deleteOnExit()
        return tempImage.absolutePath
    }
}