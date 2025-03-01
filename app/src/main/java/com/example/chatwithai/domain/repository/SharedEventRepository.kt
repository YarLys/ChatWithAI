package com.example.chatwithai.domain.repository

import com.example.chatwithai.presentation.rags.RagSharedEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SharedEventRepository {
    val _events: MutableStateFlow<RagSharedEvent?>
    val events: StateFlow<RagSharedEvent?>

    suspend fun emitEvent(ragsEvent: RagSharedEvent)
    suspend fun clearEvent()
}