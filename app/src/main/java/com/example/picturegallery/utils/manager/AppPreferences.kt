package com.example.picturegallery.utils.manager

import android.content.SharedPreferences

class AppPreferences(private val prefs: SharedPreferences) {

    var token: String
        get() {
            return prefs.getString(TOKEN_KEY, "") ?: ""
        }
        set(value) {
            prefs.edit().putString(TOKEN_KEY, value).apply()
        }

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
    }
}