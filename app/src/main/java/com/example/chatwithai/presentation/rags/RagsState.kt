package com.example.chatwithai.presentation.rags

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.RagOrder

data class RagsState(
    val rags: List<Rag> = emptyList(),
    val ragOrder: RagOrder = RagOrder.Title(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)