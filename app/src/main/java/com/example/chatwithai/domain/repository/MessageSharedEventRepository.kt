package com.example.chatwithai.domain.repository

import com.example.chatwithai.presentation.history.MessageSharedEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface MessageSharedEventRepository {
    val _events: MutableSharedFlow<MessageSharedEvent?>
    val events: SharedFlow<MessageSharedEvent?>

    suspend fun emitEvent(messageEvent: MessageSharedEvent)
    suspend fun clearEvent()
}