package com.example.chatwithai.data.repository

import com.example.chatwithai.data.local.dao.ChatDao
import com.example.chatwithai.domain.model.Chat
import com.example.chatwithai.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(
    private val dao: ChatDao
): ChatRepository {

    override fun getAllChats(): Flow<List<Chat>> {
        return dao.getAllChats()
    }

    override suspend fun getLastChat(): Chat? {
        return dao.getLastChat()
    }

    override suspend fun insertChat(chat: Chat) {
        dao.insertChat(chat)
    }

    override suspend fun deleteChat(chat: Chat) {
        dao.deleteChat(chat)
    }

    override suspend fun updateChat(chat: Chat) {
        dao.updateChat(chat)
    }
}