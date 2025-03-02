package com.example.chatwithai.presentation.rags

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.ItemsOrder

data class RagsState(
    val rags: List<Rag> = emptyList(),
    val itemsOrder: ItemsOrder = ItemsOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)