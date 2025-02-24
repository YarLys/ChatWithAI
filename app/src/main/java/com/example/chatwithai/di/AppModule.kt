package com.example.chatwithai.di

import com.example.chatwithai.common.Constants
import com.example.chatwithai.data.remote.NeuralApi
import com.example.chatwithai.data.repository.ResponseRepositoryImpl
import com.example.chatwithai.domain.repository.ResponseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class) // all dependencies in the module live as long as our application does
object AppModule {

    @Provides
    @Singleton // make sure, we will have a single instance of whatever function returns
    fun provideNeuralApi(): NeuralApi {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(NeuralApi::class.java)
    }

    @Provides
    @Singleton
    fun provideResponseRepository(api: NeuralApi): ResponseRepository {
        return ResponseRepositoryImpl(api)
    }
}