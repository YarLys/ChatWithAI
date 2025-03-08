package com.example.chatwithai.domain.use_case.chats

import com.example.chatwithai.domain.model.Chat
import com.example.chatwithai.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChats @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<List<Chat>> {
        return repository.getAllChats()
    }
}