package com.example.chatwithai.data.repository

import android.util.Log
import com.example.chatwithai.domain.repository.SharedEventRepository
import com.example.chatwithai.presentation.rags.RagSharedEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SharedEventRepositoryImpl: SharedEventRepository {
    override val _events = MutableSharedFlow<RagSharedEvent?>(replay = 10)   // user can add 10 rags from rag screen before he returns to the chat screen
    override val events: SharedFlow<RagSharedEvent?> = _events

    override suspend fun emitEvent(ragsEvent: RagSharedEvent) {
        if (ragsEvent is RagSharedEvent.UseRag) Log.d("EventRepository", "Emitted use rag event. ${ragsEvent.rag.content}")
        _events.emit(ragsEvent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearEvent() {
        _events.resetReplayCache()
    }
}