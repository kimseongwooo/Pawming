package com.kimseongwooo.pawming.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kimseongwooo.pawming.network.AnimalApiService
import com.kimseongwooo.pawming.network.ShelterInfoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import com.kimseongwooo.pawming.network.BuildConfig
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val url: HttpUrl = original.url.newBuilder()
                    .addEncodedQueryParameter("serviceKey", BuildConfig.API_KEY)
                    .addQueryParameter("_type", "json")
                    .build()
                chain.proceed(original.newBuilder().url(url).build())
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    @AbandonmentPublicRetrofit
    fun provideAbandonmentPublicRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/1543061/abandonmentPublicService_v2/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @AnimalShelterRetrofit
    fun provideAnimalShelterRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/1543061/animalShelterSrvc_v2/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAnimalApiService(@AbandonmentPublicRetrofit retrofit: Retrofit): AnimalApiService =
        retrofit.create(AnimalApiService::class.java)

    @Provides
    @Singleton
    fun provideShelterInfoApiService(@AnimalShelterRetrofit retrofit: Retrofit): ShelterInfoApiService =
        retrofit.create(ShelterInfoApiService::class.java)
}
