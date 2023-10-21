package com.example.popcorn.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.popcorn.data.datasource.local.dao.FavoriteDao
import com.example.popcorn.data.datasource.local.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FavoriteDb : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
