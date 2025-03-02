package com.example.chatwithai.presentation.history

import com.example.chatwithai.domain.util.ItemsOrder

sealed class MessageEvent {
    data class Order(val itemsOrder: ItemsOrder): MessageEvent()
    object ToggleOrderSection: MessageEvent()
}