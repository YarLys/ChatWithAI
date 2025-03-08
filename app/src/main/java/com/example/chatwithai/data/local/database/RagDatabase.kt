package com.example.chatwithai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.chatwithai.data.local.dao.ChatDao
import com.example.chatwithai.data.local.dao.MessageDao
import com.example.chatwithai.data.local.dao.RagDao
import com.example.chatwithai.domain.model.Chat
import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.model.Rag

@Database(
    entities = [Rag::class, MessageEntity::class, Chat::class],
    version = 5
)
abstract class RagDatabase: RoomDatabase() {

    abstract val ragDao: RagDao
    abstract val messageDao: MessageDao
    abstract val chatDao: ChatDao
}