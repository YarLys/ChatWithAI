package com.example.chatwithai.domain.model

data class Message(
    val text: String,
    val isRequest: Boolean  // true if it's a user's message
)