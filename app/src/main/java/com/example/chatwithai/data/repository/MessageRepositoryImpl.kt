package com.example.chatwithai.data.repository

import com.example.chatwithai.data.local.dao.MessageDao
import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val dao: MessageDao
) : MessageRepository {
    override fun getAllMessages(): Flow<List<MessageEntity>> {
        return dao.getAllMessages()
    }

    override suspend fun getMessageById(id: Int): MessageEntity? {
        return dao.getMessageById(id)
    }

    override suspend fun insertMessage(messageEntity: MessageEntity) {
        dao.insertMessage(messageEntity)
    }

    override suspend fun deleteMessage(messageEntity: MessageEntity) {
        dao.deleteMessage(messageEntity)
    }
}