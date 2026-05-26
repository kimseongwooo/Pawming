package com.kimseongwooo.pawming.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_animals")
data class FavoriteAnimalEntity(
    @PrimaryKey val desertionNo: String,
    val kindNm: String,
    val sexCd: String,
    val age: String,
    val happenPlace: String,
    val processState: String,
    val imageUrl: String,
    val savedAt: Long
)
