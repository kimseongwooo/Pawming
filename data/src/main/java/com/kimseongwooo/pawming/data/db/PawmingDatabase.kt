package com.kimseongwooo.pawming.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteAnimalEntity::class], version = 1, exportSchema = false)
abstract class PawmingDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
}
