package com.example.chatwithai.presentation.history

import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.util.ItemsOrder

sealed class MessageEvent {
    data class Order(val itemsOrder: ItemsOrder): MessageEvent()
    data class DeleteMessage(val message: MessageEntity): MessageEvent()
    data class UpdateMessage(val message: MessageEntity): MessageEvent()
    object ToggleOrderSection: MessageEvent()
    object DeleteHistory: MessageEvent()
    object RestoreMessage: MessageEvent()
}