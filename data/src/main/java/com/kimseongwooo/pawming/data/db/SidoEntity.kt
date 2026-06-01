package com.kimseongwooo.pawming.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sido")
data class SidoEntity(
    @PrimaryKey val orgCd: String,
    val orgdownNm: String
)
