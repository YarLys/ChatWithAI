package com.example.chatwithai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatwithai.data.local.dao.RagDao
import com.example.chatwithai.domain.model.Rag

@Database(
    entities = [Rag::class],
    version = 1
)
abstract class RagDatabase: RoomDatabase() {

    abstract val ragDao: RagDao
}