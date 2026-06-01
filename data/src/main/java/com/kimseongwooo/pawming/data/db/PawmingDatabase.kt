package com.kimseongwooo.pawming.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        FavoriteAnimalEntity::class,
        SidoEntity::class,
        SigunguEntity::class,
        SigunguFetchLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PawmingDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
    abstract fun sidoDao(): SidoDao
    abstract fun sigunguDao(): SigunguDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `sido` (`orgCd` TEXT NOT NULL, `orgdownNm` TEXT NOT NULL, PRIMARY KEY(`orgCd`))")
                db.execSQL("CREATE TABLE IF NOT EXISTS `sigungu` (`orgCd` TEXT NOT NULL, `orgdownNm` TEXT NOT NULL, `uprCd` TEXT NOT NULL, PRIMARY KEY(`orgCd`))")
                db.execSQL("CREATE TABLE IF NOT EXISTS `sigungu_fetch_log` (`uprCd` TEXT NOT NULL, PRIMARY KEY(`uprCd`))")
            }
        }
    }
}
