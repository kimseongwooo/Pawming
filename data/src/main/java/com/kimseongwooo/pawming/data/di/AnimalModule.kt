package com.kimseongwooo.pawming.data.di

import com.kimseongwooo.pawming.data.datasource.AnimalRemoteDataSource
import com.kimseongwooo.pawming.data.datasource.AnimalRemoteDataSourceImpl
import com.kimseongwooo.pawming.data.repository.AnimalRepositoryImpl
import com.kimseongwooo.pawming.domain.repository.AnimalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnimalModule {

    @Binds
    @Singleton
    abstract fun bindAnimalRemoteDataSource(impl: AnimalRemoteDataSourceImpl): AnimalRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAnimalRepository(impl: AnimalRepositoryImpl): AnimalRepository
}
