package com.example.picturegallery.data.manager

import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.picturegallery.domain.manager.CryptoManager
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor() : CryptoManager {

    private companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
        private const val ENCRYPTED_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$ENCRYPTED_PADDING"
    }

    private val secretKey = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES).run {
        init(256)
        generateKey().encoded
    }

    override fun encrypt(key: CryptoManager.Key, value: String): String? {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val secretKeySpec = SecretKeySpec(secretKey, KeyProperties.KEY_ALGORITHM_AES)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val bytes = cipher.doFinal(value.toByteArray())

            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    override fun decrypt(key: CryptoManager.Key, value: String): String? =
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val secretKeySpec = SecretKeySpec(secretKey, KeyProperties.KEY_ALGORITHM_AES)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val encryptedData = Base64.decode(value, Base64.DEFAULT)
            val decryptedValue = cipher.doFinal(encryptedData)
            String(decryptedValue)

        } catch(e: Exception) {
            null
        }
}