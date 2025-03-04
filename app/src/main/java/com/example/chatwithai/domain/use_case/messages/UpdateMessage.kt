package com.example.chatwithai.domain.use_case.messages

import android.util.Log
import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.repository.MessageRepository
import javax.inject.Inject

class UpdateMessage @Inject constructor(
    private val repository: MessageRepository
){
    suspend operator fun invoke(message: MessageEntity) {
        Log.d("MessageRepository", "Update message: ${message.request}, isStarred: ${message.isStarred}")
        repository.updateMessage(message)
    }
}