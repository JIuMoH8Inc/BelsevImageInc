package com.example.picturegallery.utils.extensions

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.IOException

@Throws(IOException::class)
fun Uri.getByteArray(context: Context): ByteArray? =
    context.contentResolver.openInputStream(this)?.buffered()?.use { inputStream ->
        inputStream.readBytes()
    }

fun Uri.getFileName(context: Context): String? {
    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()

        return cursor.getString(nameIndex)

    } ?: run {
        return null
    }
}