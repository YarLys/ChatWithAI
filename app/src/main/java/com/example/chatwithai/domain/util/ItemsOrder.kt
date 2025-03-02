package com.example.chatwithai.domain.util

sealed class ItemsOrder(
    val orderType: OrderType
) {
    class Title(orderType: OrderType): ItemsOrder(orderType)
    class Date(orderType: OrderType): ItemsOrder(orderType)

    fun copy(orderType: OrderType): ItemsOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}