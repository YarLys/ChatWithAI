package com.example.chatwithai.domain.repository

import com.example.chatwithai.presentation.rags.RagSharedEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface SharedEventRepository {
    val _events: MutableSharedFlow<RagSharedEvent?>
    val events: SharedFlow<RagSharedEvent?>

    suspend fun emitEvent(ragsEvent: RagSharedEvent)
    suspend fun clearEvent()
}