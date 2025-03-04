package com.example.chatwithai.domain.use_case.messages

import com.example.chatwithai.domain.repository.MessageSharedEventRepository
import com.example.chatwithai.presentation.history.MessageSharedEvent
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class UseMessage @Inject constructor(
    private val repository: MessageSharedEventRepository
) {
    suspend fun sendEvent(messageEvent: MessageSharedEvent) {
        repository.emitEvent(messageEvent)
    }

    fun observeEvents(): SharedFlow<MessageSharedEvent?> {
        return repository.events
    }

    suspend fun clearEvent() {
        repository.clearEvent()
    }
}