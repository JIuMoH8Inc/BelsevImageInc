package com.example.picturegallery.domain.model.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.picturegallery.data.data_source.TokenDao
import com.example.picturegallery.domain.model.room.entity.Token


@Database(entities = [Token::class], version = 1)
abstract class LocalDB: RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}