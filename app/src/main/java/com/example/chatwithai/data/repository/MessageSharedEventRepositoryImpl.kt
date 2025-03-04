package com.example.chatwithai.data.repository

import android.util.Log
import com.example.chatwithai.domain.repository.MessageSharedEventRepository
import com.example.chatwithai.presentation.history.MessageSharedEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class MessageSharedEventRepositoryImpl: MessageSharedEventRepository {
    override val _events = MutableSharedFlow<MessageSharedEvent?>(replay = 10)
    override val events: SharedFlow<MessageSharedEvent?> = _events

    override suspend fun emitEvent(messageEvent: MessageSharedEvent) {
        if (messageEvent is MessageSharedEvent.UseMessage) Log.d("MessageEventRepository", "Emmited use message event: ${messageEvent.message.request}")
        _events.emit(messageEvent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clearEvent() {
        _events.resetReplayCache()
    }
}