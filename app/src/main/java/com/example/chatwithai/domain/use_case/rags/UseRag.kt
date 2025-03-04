package com.example.chatwithai.domain.use_case.rags

import com.example.chatwithai.domain.repository.RagSharedEventRepository
import com.example.chatwithai.presentation.rags.RagSharedEvent
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class UseRag @Inject constructor(
    private val repository: RagSharedEventRepository
) {
    suspend fun sendEvent(ragEvent: RagSharedEvent) {
        repository.emitEvent(ragEvent)
    }

    fun observeEvents(): SharedFlow<RagSharedEvent?> {
        return repository.events
    }

    suspend fun clearEvent() {
        repository.clearEvent()
    }
}