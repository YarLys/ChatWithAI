package com.example.chatwithai.data.repository

import android.util.Log
import com.example.chatwithai.domain.repository.SharedEventRepository
import com.example.chatwithai.presentation.rags.RagSharedEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedEventRepositoryImpl: SharedEventRepository {
    override val _events = MutableStateFlow<RagSharedEvent?>(null)
    override val events: StateFlow<RagSharedEvent?> = _events

    override suspend fun emitEvent(ragsEvent: RagSharedEvent) {
        if (ragsEvent is RagSharedEvent.UseRag) Log.d("EventRepository", "Emitted use rag event. ${ragsEvent.rag.content}")
        _events.emit(ragsEvent)
    }

    override suspend fun clearEvent() {
        _events.emit(null)
    }
}