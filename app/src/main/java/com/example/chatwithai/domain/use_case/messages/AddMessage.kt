package com.example.chatwithai.domain.use_case.messages

import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.repository.MessageRepository
import javax.inject.Inject

class AddMessage @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(message: MessageEntity) {
        repository.insertMessage(message)
    }
}