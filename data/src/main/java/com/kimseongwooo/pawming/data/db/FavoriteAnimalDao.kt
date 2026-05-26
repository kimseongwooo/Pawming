package com.kimseongwooo.pawming.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimalDao {

    @Query("SELECT * FROM favorite_animals ORDER BY savedAt DESC")
    fun getFavorites(): Flow<List<FavoriteAnimalEntity>>

    @Query("SELECT desertionNo FROM favorite_animals")
    fun getFavoriteIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteAnimalEntity)

    @Query("DELETE FROM favorite_animals WHERE desertionNo = :desertionNo")
    suspend fun delete(desertionNo: String)
}
