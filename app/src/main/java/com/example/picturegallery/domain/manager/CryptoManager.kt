package com.example.picturegallery.domain.manager

interface CryptoManager {
    fun encrypt(key: Key, value: String) : String?
    fun decrypt(key: Key, value: String) : String?

    enum class Key {
        TOKEN, PIN
    }
}