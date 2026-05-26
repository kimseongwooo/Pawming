package com.kimseongwooo.pawming.data.di

import android.content.Context
import androidx.room.Room
import com.kimseongwooo.pawming.data.db.FavoriteAnimalDao
import com.kimseongwooo.pawming.data.db.PawmingDatabase
import com.kimseongwooo.pawming.data.repository.FavoriteRepositoryImpl
import com.kimseongwooo.pawming.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteDatabaseModule {

    @Provides
    @Singleton
    fun providePawmingDatabase(@ApplicationContext context: Context): PawmingDatabase =
        Room.databaseBuilder(context, PawmingDatabase::class.java, "pawming.db").build()

    @Provides
    @Singleton
    fun provideFavoriteAnimalDao(db: PawmingDatabase): FavoriteAnimalDao = db.favoriteAnimalDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}
