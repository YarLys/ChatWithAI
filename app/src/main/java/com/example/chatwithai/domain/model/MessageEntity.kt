package com.example.chatwithai.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class MessageEntity(
    val request: String, // user's message
    val response: String, // ai's response
    val timestamp: Long, // date
    val isStarred: Boolean = false,
    @PrimaryKey val id: Int? = null
)