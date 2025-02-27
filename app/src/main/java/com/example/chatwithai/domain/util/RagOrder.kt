package com.example.chatwithai.domain.util

sealed class RagOrder(
    val orderType: OrderType
) {
    class Title(orderType: OrderType): RagOrder(orderType)
    class Date(orderType: OrderType): RagOrder(orderType)

    fun copy(orderType: OrderType): RagOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}