package com.example.popcorn.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popcorn.data.datasource.local.model.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: FavoriteEntity)

    @Query("SELECT * FROM favorite_table")
    fun getAllFavorites(): List<FavoriteEntity?>

    @Query("DELETE FROM favorite_table WHERE id = :id")
    fun delete(id: Int)
}
