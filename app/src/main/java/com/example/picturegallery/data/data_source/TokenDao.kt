package com.example.picturegallery.data.data_source

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.Update
import com.example.picturegallery.domain.model.room.db.LocalDB
import com.example.picturegallery.domain.model.room.entity.Token

@Dao
interface TokenDao {

    @Query("SELECT * FROM token WHERE id = 1")
    fun getToken(): Token?

    @Insert
    fun saveToken(token: Token)

    @Update
    fun updateToken(token: Token)

    companion object {
        @JvmStatic
        fun create(context: Context): TokenDao =
            Room.databaseBuilder(context, LocalDB::class.java, "local_db").build().tokenDao()
    }
}