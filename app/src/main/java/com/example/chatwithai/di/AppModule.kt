package com.example.chatwithai.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.chatwithai.common.Constants
import com.example.chatwithai.common.Constants.DATABASE_NAME
import com.example.chatwithai.data.local.database.RagDatabase
import com.example.chatwithai.data.remote.NeuralApi
import com.example.chatwithai.data.repository.MessageRepositoryImpl
import com.example.chatwithai.data.repository.RagRepositoryImpl
import com.example.chatwithai.data.repository.ResponseRepositoryImpl
import com.example.chatwithai.data.repository.SharedEventRepositoryImpl
import com.example.chatwithai.domain.repository.MessageRepository
import com.example.chatwithai.domain.repository.RagRepository
import com.example.chatwithai.domain.repository.ResponseRepository
import com.example.chatwithai.domain.repository.SharedEventRepository
import com.example.chatwithai.domain.use_case.messages.GetMessages
import com.example.chatwithai.domain.use_case.messages.MessageUseCases
import com.example.chatwithai.domain.use_case.rags.AddRag
import com.example.chatwithai.domain.use_case.rags.DeleteRag
import com.example.chatwithai.domain.use_case.rags.GetRag
import com.example.chatwithai.domain.use_case.rags.GetRags
import com.example.chatwithai.domain.use_case.rags.RagUseCases
import com.example.chatwithai.domain.use_case.rags.UseRag
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

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // create new table for messages
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                request TEXT NOT NULL,
                response TEXT NOT NULL,
                timestamp INTEGER NOT NULL
            )
            """
            )
        }
    }

    @Provides
    @Singleton
    fun provideRagDatabase(app: Application): RagDatabase {
        return Room.databaseBuilder(
            app,
            RagDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideRagRepository(db: RagDatabase): RagRepository {
        return RagRepositoryImpl(db.ragDao)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(db: RagDatabase): MessageRepository {
        return MessageRepositoryImpl(db.messageDao)
    }

    @Provides
    @Singleton
    fun provideRagUseCases(repository: RagRepository): RagUseCases {
        return RagUseCases(
            getRags = GetRags(repository),
            getRag = GetRag(repository),
            addRag = AddRag(repository),
            deleteRag = DeleteRag(repository)
        )
    }

    @Provides
    @Singleton
    fun provideMessageUseCases(repository: MessageRepository): MessageUseCases {
        return MessageUseCases(
            getMessages = GetMessages(repository)
        )
    }

    @Provides
    @Singleton
    fun provideUseRagUseCase(repository: SharedEventRepository): UseRag {
        return UseRag(repository)
    }

    @Provides
    @Singleton
    fun provideSharedEventRepository(): SharedEventRepository {
        return SharedEventRepositoryImpl()
    }
}