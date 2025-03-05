package com.example.chatwithai.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rag(
    val title: String, // goal, role, environment
    val content: String,   // what's inside rag
    val timestamp: Long,  // date
    val isStarred: Boolean = false,
    @PrimaryKey val id: Int? = null
)

class InvalidRagException(message: String): Exception(message)