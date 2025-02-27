package com.example.chatwithai.presentation.rags.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.RagOrder

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    ragOrder: RagOrder = RagOrder.Title(OrderType.Descending),
    onOrderChange: (RagOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(   // order by title
                text = "По заголовку",
                selected = ragOrder is RagOrder.Title,
                onSelect = { onOrderChange(RagOrder.Title(ragOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(   // order by date
                text = "По дате",
                selected = ragOrder is RagOrder.Date,
                onSelect = { onOrderChange(RagOrder.Date(ragOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(   // ascending order
                text = "По возрастанию",
                selected = ragOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(ragOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(   // descending order
                text = "По убыванию",
                selected = ragOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(ragOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}