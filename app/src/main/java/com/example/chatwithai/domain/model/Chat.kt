package com.example.chatwithai.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    val title: String,
    @PrimaryKey val id: Int? = null
)