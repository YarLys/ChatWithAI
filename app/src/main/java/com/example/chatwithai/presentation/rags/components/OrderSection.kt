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
import com.example.chatwithai.domain.util.ItemsOrder

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    itemsOrder: ItemsOrder = ItemsOrder.Title(OrderType.Descending),
    onOrderChange: (ItemsOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(   // order by title
                text = "По заголовку",
                selected = itemsOrder is ItemsOrder.Title,
                onSelect = { onOrderChange(ItemsOrder.Title(itemsOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(   // order by date
                text = "По дате",
                selected = itemsOrder is ItemsOrder.Date,
                onSelect = { onOrderChange(ItemsOrder.Date(itemsOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(   // ascending order
                text = "По возрастанию",
                selected = itemsOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(itemsOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(   // descending order
                text = "По убыванию",
                selected = itemsOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(itemsOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}