package com.example.chatwithai.domain.use_case.chats

import com.example.chatwithai.domain.model.Chat
import com.example.chatwithai.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteChat @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat) {
        repository.deleteChat(chat)
    }
}