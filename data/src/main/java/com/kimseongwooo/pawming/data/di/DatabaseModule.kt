package com.kimseongwooo.pawming.data.di

import android.content.Context
import androidx.room.Room
import com.kimseongwooo.pawming.data.db.FavoriteAnimalDao
import com.kimseongwooo.pawming.data.db.PawmingDatabase
import com.kimseongwooo.pawming.data.db.PawmingDatabase.Companion.MIGRATION_1_2
import com.kimseongwooo.pawming.data.db.SidoDao
import com.kimseongwooo.pawming.data.db.SigunguDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePawmingDatabase(@ApplicationContext context: Context): PawmingDatabase =
        Room.databaseBuilder(context, PawmingDatabase::class.java, "pawming.db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideFavoriteAnimalDao(db: PawmingDatabase): FavoriteAnimalDao = db.favoriteAnimalDao()

    @Provides
    @Singleton
    fun provideSidoDao(db: PawmingDatabase): SidoDao = db.sidoDao()

    @Provides
    @Singleton
    fun provideSigunguDao(db: PawmingDatabase): SigunguDao = db.sigunguDao()
}
