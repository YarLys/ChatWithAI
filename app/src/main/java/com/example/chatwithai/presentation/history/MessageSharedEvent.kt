package com.example.chatwithai.presentation.history

import com.example.chatwithai.domain.model.MessageEntity

sealed class MessageSharedEvent {
    data class UseMessage(val message: MessageEntity): MessageSharedEvent()
}
