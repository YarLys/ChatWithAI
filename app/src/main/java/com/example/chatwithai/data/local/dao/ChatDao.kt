package com.example.chatwithai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chatwithai.domain.model.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * from chats")
    fun getAllChats(): Flow<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    @Delete
    suspend fun deleteChat(chat: Chat)

    @Update
    suspend fun updateChat(chat: Chat)
}