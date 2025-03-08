package com.example.chatwithai.domain.repository

import com.example.chatwithai.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getAllChats(): Flow<List<Chat>>

    suspend fun getLastChat(): Chat?

    suspend fun insertChat(chat: Chat)

    suspend fun deleteChat(chat: Chat)

    suspend fun updateChat(chat: Chat)
}