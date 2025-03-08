package com.example.chatwithai.di

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.chatwithai.common.Constants
import com.example.chatwithai.common.Constants.DATABASE_NAME
import com.example.chatwithai.data.local.database.RagDatabase
import com.example.chatwithai.data.remote.NeuralApi
import com.example.chatwithai.data.repository.ChatRepositoryImpl
import com.example.chatwithai.data.repository.MessageRepositoryImpl
import com.example.chatwithai.data.repository.MessageSharedEventRepositoryImpl
import com.example.chatwithai.data.repository.RagRepositoryImpl
import com.example.chatwithai.data.repository.ResponseRepositoryImpl
import com.example.chatwithai.data.repository.RagSharedEventRepositoryImpl
import com.example.chatwithai.data.repository.UserPreferencesImpl
import com.example.chatwithai.domain.repository.ChatRepository
import com.example.chatwithai.domain.repository.MessageRepository
import com.example.chatwithai.domain.repository.MessageSharedEventRepository
import com.example.chatwithai.domain.repository.RagRepository
import com.example.chatwithai.domain.repository.ResponseRepository
import com.example.chatwithai.domain.repository.RagSharedEventRepository
import com.example.chatwithai.domain.repository.UserPreferences
import com.example.chatwithai.domain.use_case.chats.AddChat
import com.example.chatwithai.domain.use_case.chats.ChatUseCases
import com.example.chatwithai.domain.use_case.chats.DeleteChat
import com.example.chatwithai.domain.use_case.chats.GetChats
import com.example.chatwithai.domain.use_case.chats.UpdateChat
import com.example.chatwithai.domain.use_case.messages.AddMessage
import com.example.chatwithai.domain.use_case.messages.DeleteAllMessages
import com.example.chatwithai.domain.use_case.messages.DeleteMessage
import com.example.chatwithai.domain.use_case.messages.GetMessage
import com.example.chatwithai.domain.use_case.messages.GetMessages
import com.example.chatwithai.domain.use_case.messages.GetMessagesByChatId
import com.example.chatwithai.domain.use_case.messages.GetStarredMessages
import com.example.chatwithai.domain.use_case.messages.MessageUseCases
import com.example.chatwithai.domain.use_case.messages.UpdateMessage
import com.example.chatwithai.domain.use_case.messages.UseMessage
import com.example.chatwithai.domain.use_case.notifications.CheckIfUserUsedAppToday
import com.example.chatwithai.domain.use_case.notifications.SendNotification
import com.example.chatwithai.domain.use_case.rags.AddRag
import com.example.chatwithai.domain.use_case.rags.DeleteRag
import com.example.chatwithai.domain.use_case.rags.GetRag
import com.example.chatwithai.domain.use_case.rags.GetRags
import com.example.chatwithai.domain.use_case.rags.GetStarredRags
import com.example.chatwithai.domain.use_case.rags.RagUseCases
import com.example.chatwithai.domain.use_case.rags.UpdateRag
import com.example.chatwithai.domain.use_case.rags.UseRag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
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

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                ALTER TABLE history
                ADD COLUMN isStarred INTEGER NOT NULL DEFAULT 0
                """
            )
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                ALTER TABLE rag
                ADD COLUMN isStarred INTEGER NOT NULL DEFAULT 0
                """
            )
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // create new table for chats
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS chats (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL
            )
            """
            )
            database.execSQL(
                """
            ALTER TABLE history ADD COLUMN chatId INTEGER NOT NULL DEFAULT 1 
            """
            )
            database.execSQL(
                """
            INSERT OR IGNORE INTO chats (id, title) VALUES (1, 'Основной чат')
            """
            )
            database.execSQL(
                """
            UPDATE history SET chatId = 1 
            """
            )

            database.execSQL(
                """
                    CREATE TRIGGER fk_history_chatId
                    BEFORE INSERT ON history
                    FOR EACH ROW
                    BEGIN
                        SELECT CASE
                            WHEN (SELECT id FROM chats WHERE id = NEW.chatId) IS NULL
                            THEN RAISE(ABORT, 'Foreign key violation: chatId does not exist')
                        END;
                    END;
                    """
            )
            // create new table for history
            database.execSQL(
                """
            CREATE TABLE history_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                request TEXT NOT NULL,
                response TEXT NOT NULL,
                timestamp INTEGER NOT NULL,
                isStarred INTEGER NOT NULL DEFAULT 0,
                chatId INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY (chatId) REFERENCES chats(id) ON DELETE CASCADE
            )
            """
            )
            // copy data from old table to new one
            database.execSQL(
                """
            INSERT INTO history_new (id, request, response, timestamp, isStarred, chatId)
            SELECT id, request, response, timestamp, isStarred, chatId FROM history
            """
            )
            // delete old table and rename new one
            database.execSQL("DROP TABLE history")
            database.execSQL("ALTER TABLE history_new RENAME TO history")
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
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
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
    fun provideChatRepository(db: RagDatabase): ChatRepository {
        return ChatRepositoryImpl(db.chatDao)
    }

    @Provides
    @Singleton
    fun provideRagUseCases(repository: RagRepository): RagUseCases {
        return RagUseCases(
            getRags = GetRags(repository),
            getRag = GetRag(repository),
            addRag = AddRag(repository),
            deleteRag = DeleteRag(repository),
            updateRag = UpdateRag(repository),
            getStarredRags = GetStarredRags(repository)
        )
    }

    @Provides
    @Singleton
    fun provideMessageUseCases(repository: MessageRepository): MessageUseCases {
        return MessageUseCases(
            getMessages = GetMessages(repository),
            deleteMessage = DeleteMessage(repository),
            deleteAllMessages = DeleteAllMessages(repository),
            getMessage = GetMessage(repository),
            addMessage = AddMessage(repository),
            updateMessage = UpdateMessage(repository),
            getStarredMessages = GetStarredMessages(repository)
        )
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases {
        return ChatUseCases(
            getChats = GetChats(repository),
            addChat = AddChat(repository),
            deleteChat = DeleteChat(repository),
            updateChat = UpdateChat(repository)
        )
    }

    @Provides
    @Singleton
    fun provideUseRagUseCase(repository: RagSharedEventRepository): UseRag {
        return UseRag(repository)
    }

    @Provides
    @Singleton
    fun provideUseMessageUseCase(repository: MessageSharedEventRepository): UseMessage {
        return UseMessage(repository)
    }

    @Provides
    @Singleton
    fun provideRagSharedEventRepository(): RagSharedEventRepository {
        return RagSharedEventRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideMessageSharedEventRepository(): MessageSharedEventRepository {
        return MessageSharedEventRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferencesImpl(context)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @Provides
    @Singleton
    fun provideCheckIfUserUsedAppTodayUseCase(userPreferences: UserPreferences): CheckIfUserUsedAppToday {
        return CheckIfUserUsedAppToday(userPreferences)
    }

    @Provides
    @Singleton
    fun provideSendNotificationUseCase(notificationManager: NotificationManagerCompat): SendNotification {
        return SendNotification(notificationManager)
    }
}