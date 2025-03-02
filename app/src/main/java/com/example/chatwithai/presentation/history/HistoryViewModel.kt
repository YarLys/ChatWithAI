package com.example.chatwithai.presentation.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.domain.use_case.messages.MessageUseCases
import com.example.chatwithai.domain.util.ItemsOrder
import com.example.chatwithai.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val messageUseCases: MessageUseCases
): ViewModel() {

    private val _state = mutableStateOf(MessagesState())
    val state: State<MessagesState> = _state

    private var getMessagesJob: Job? = null

    init {
        getMessages(ItemsOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.Order -> {
                if (state.value.itemsOrder::class == event.itemsOrder::class &&   // no order changes
                    state.value.itemsOrder.orderType == event.itemsOrder.orderType) {
                    return
                }
                getMessages(event.itemsOrder)
            }
            is MessageEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getMessages(itemsOrder: ItemsOrder) {
        getMessagesJob?.cancel()
        getMessagesJob = messageUseCases.getMessages(itemsOrder).onEach { messages ->
            _state.value = state.value.copy(
                messages = messages,
                itemsOrder = itemsOrder
            )
        }.launchIn(viewModelScope)
    }
}