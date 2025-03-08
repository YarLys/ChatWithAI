package com.example.chatwithai.domain.use_case.messages

import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesByChatId @Inject constructor(
    private val repository: MessageRepository
) {

    operator fun invoke(chatId: Int): Flow<List<MessageEntity>> {
        return repository.getMessagesByChatId(chatId)
    }
}