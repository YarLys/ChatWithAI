package com.example.chatwithai.domain.use_case

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.RagOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRags @Inject constructor (
    private val repository: RagRepository
) {
    operator fun invoke(
        ragOrder: RagOrder = RagOrder.Title(OrderType.Descending) // descending order by title by default
    ): Flow<List<Rag>> {
        return repository.getAllRags().map {rags ->
            when (ragOrder.orderType) {
                is OrderType.Ascending -> {
                    when (ragOrder) {
                        is RagOrder.Title -> rags.sortedBy { it.title.lowercase() }
                        is RagOrder.Date -> rags.sortedBy { it.timestamp }
                    }
                }
                is OrderType.Descending -> {
                    when (ragOrder) {
                        is RagOrder.Title -> rags.sortedByDescending { it.title.lowercase() }
                        is RagOrder.Date -> rags.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}