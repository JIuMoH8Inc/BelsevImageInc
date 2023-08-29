package com.example.picturegallery.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class PictureUtils {
    companion object {
        fun getPhotoBitmap(base64: String): Bitmap {
            val decodeString = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
        }
    }
}