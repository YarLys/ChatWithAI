package com.example.chatwithai.domain.use_case.messages

import com.example.chatwithai.domain.repository.MessageRepository
import javax.inject.Inject

class DeleteAllMessages @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllMessages()
    }
}