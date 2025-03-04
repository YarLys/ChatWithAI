package com.example.chatwithai.domain.repository

import com.example.chatwithai.domain.model.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getAllMessages(): Flow<List<MessageEntity>>

    suspend fun getMessageById(id: Int): MessageEntity?

    suspend fun insertMessage(messageEntity: MessageEntity)

    suspend fun deleteMessage(messageEntity: MessageEntity)

    suspend fun updateMessage(messageEntity: MessageEntity)

    suspend fun deleteAllMessages()

    fun getStarredMessages(): Flow<List<MessageEntity>>
}