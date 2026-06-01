package com.kimseongwooo.pawming.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SigunguDao {

    @Query("SELECT * FROM sigungu WHERE uprCd = :uprCd")
    suspend fun getByUprCd(uprCd: String): List<SigunguEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SigunguEntity>)

    @Query("SELECT COUNT(*) FROM sigungu_fetch_log WHERE uprCd = :uprCd")
    suspend fun fetchedCount(uprCd: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markFetched(log: SigunguFetchLogEntity)
}
