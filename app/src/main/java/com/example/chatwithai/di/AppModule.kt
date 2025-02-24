package com.example.chatwithai.di

import com.example.chatwithai.common.Constants
import com.example.chatwithai.data.remote.NeuralApi
import com.example.chatwithai.data.repository.ResponseRepositoryImpl
import com.example.chatwithai.domain.repository.ResponseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // all dependencies in the module live as long as our application does
object AppModule {

    @Provides
    @Singleton // make sure, we will have a single instance of whatever function returns
    fun provideNeuralApi(): NeuralApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NeuralApi::class.java)
    }

    @Provides
    @Singleton
    fun provideResponseRepository(api: NeuralApi): ResponseRepository {
        return ResponseRepositoryImpl(api)
    }
}