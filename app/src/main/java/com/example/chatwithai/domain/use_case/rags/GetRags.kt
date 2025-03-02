package com.example.chatwithai.domain.use_case.rags

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.ItemsOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRags @Inject constructor (
    private val repository: RagRepository
) {
    operator fun invoke(
        itemsOrder: ItemsOrder = ItemsOrder.Date(OrderType.Descending) // descending order by date by default
    ): Flow<List<Rag>> {
        return repository.getAllRags().map {rags ->
            when (itemsOrder.orderType) {
                is OrderType.Ascending -> {
                    when (itemsOrder) {
                        is ItemsOrder.Title -> rags.sortedBy { it.title.lowercase() }
                        is ItemsOrder.Date -> rags.sortedBy { it.timestamp }
                    }
                }
                is OrderType.Descending -> {
                    when (itemsOrder) {
                        is ItemsOrder.Title -> rags.sortedByDescending { it.title.lowercase() }
                        is ItemsOrder.Date -> rags.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}