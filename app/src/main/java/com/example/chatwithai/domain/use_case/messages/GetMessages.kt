package com.example.chatwithai.domain.use_case.messages

import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.repository.MessageRepository
import com.example.chatwithai.domain.util.ItemsOrder
import com.example.chatwithai.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMessages @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(
        itemsOrder: ItemsOrder = ItemsOrder.Date(OrderType.Descending)
    ): Flow<List<MessageEntity>> {
        return repository.getAllMessages().map { messages ->
            when (itemsOrder.orderType) {
                is OrderType.Ascending -> {
                    when (itemsOrder) {
                        is ItemsOrder.Title -> messages.sortedBy { it.request.lowercase() }
                        is ItemsOrder.Date -> messages.sortedBy { it.timestamp }
                    }
                }
                is OrderType.Descending -> {
                    when (itemsOrder) {
                        is ItemsOrder.Title -> messages.sortedByDescending { it.request.lowercase() }
                        is ItemsOrder.Date -> messages.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}