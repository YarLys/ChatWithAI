package com.example.chatwithai.presentation.rags

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.util.ItemsOrder

sealed class RagsEvent {
    data class Order(val itemsOrder: ItemsOrder): RagsEvent()   // order changes
    data class DeleteRag(val rag: Rag): RagsEvent()
    object RestoreRag: RagsEvent()   // undo deleting rag
    object ToggleOrderSection: RagsEvent()  // is order section visible or not
}
