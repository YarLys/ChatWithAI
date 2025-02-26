package com.example.chatwithai.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
