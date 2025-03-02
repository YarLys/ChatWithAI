package com.example.chatwithai.presentation.history

import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.util.ItemsOrder
import com.example.chatwithai.domain.util.OrderType

data class MessagesState(
    val messages: List<MessageEntity> = emptyList(),
    val itemsOrder: ItemsOrder = ItemsOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
