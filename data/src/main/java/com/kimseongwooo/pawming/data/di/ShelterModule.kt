package com.kimseongwooo.pawming.data.di

import com.kimseongwooo.pawming.data.datasource.ShelterRemoteDataSource
import com.kimseongwooo.pawming.data.datasource.ShelterRemoteDataSourceImpl
import com.kimseongwooo.pawming.data.repository.ShelterRepositoryImpl
import com.kimseongwooo.pawming.domain.repository.ShelterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ShelterModule {

    @Binds
    @Singleton
    abstract fun bindShelterRemoteDataSource(impl: ShelterRemoteDataSourceImpl): ShelterRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindShelterRepository(impl: ShelterRepositoryImpl): ShelterRepository
}
