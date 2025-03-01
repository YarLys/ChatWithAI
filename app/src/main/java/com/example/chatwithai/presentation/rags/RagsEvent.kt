package com.example.chatwithai.presentation.rags

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.util.RagOrder

sealed class RagsEvent {
    data class Order(val ragOrder: RagOrder): RagsEvent()   // order changes 
    data class DeleteRag(val rag: Rag): RagsEvent()
    //data class UseRag(val rag: Rag): RagsEvent() // if user wants to use rag in request
    object RestoreRag: RagsEvent()   // undo deleting rag
    object ToggleOrderSection: RagsEvent()  // is order section visible or not
}
