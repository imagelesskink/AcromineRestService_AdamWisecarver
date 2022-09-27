package com.example.acrominerestservice_adamwisecarver.hilt

import com.example.acrominerestservice_adamwisecarver.data.api.AcromineRepository
import com.example.acrominerestservice_adamwisecarver.data.api.AcromineRepositoryImpl
import com.example.acrominerestservice_adamwisecarver.data.api.ApiService
import com.example.acrominerestservice_adamwisecarver.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Singleton
    @Provides
    fun provideApiService(): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRepository(): AcromineRepository = AcromineRepositoryImpl(provideApiService())

    @Singleton
    @Provides
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}