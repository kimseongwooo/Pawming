package com.kimseongwooo.pawming.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SidoDao {

    @Query("SELECT * FROM sido")
    suspend fun getAll(): List<SidoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SidoEntity>)
}
