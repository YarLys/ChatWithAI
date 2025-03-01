package com.example.chatwithai.presentation.rags

import com.example.chatwithai.domain.model.Rag

sealed class RagSharedEvent {
    data class UseRag(val rag: Rag): RagSharedEvent()
}