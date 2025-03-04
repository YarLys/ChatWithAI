package com.example.chatwithai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chatwithai.domain.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM history")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getMessageById(id: Int): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageEntity: MessageEntity)

    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)

    @Query("DELETE FROM history")
    suspend fun deleteAllMessages()

    @Query("SELECT * FROM history WHERE isStarred = 1")
    fun getStarredMessages(): Flow<List<MessageEntity>>
}