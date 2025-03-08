package com.example.chatwithai.presentation.chat

import com.example.chatwithai.domain.model.Chat

sealed class ChatEvent {
    data class ChangeChat(val chatId: Int?): ChatEvent()
    data class AddChat(val chat: Chat): ChatEvent()
    data class DeleteChat(val chat: Chat): ChatEvent()
    data class UpdateChat(val chat: Chat): ChatEvent()
}
