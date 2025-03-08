package com.example.chatwithai.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE  // delete messages when deleting chat
        )
    ]
)
data class MessageEntity(
    val request: String, // user's message
    val response: String, // ai's response
    val timestamp: Long, // date
    val isStarred: Boolean = false,
    val chatId: Int,   // foreignkey on chatId from chats, default - 1st chat
    @PrimaryKey val id: Int? = null
)