package com.example.chatwithai.presentation.history

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.use_case.messages.MessageUseCases
import com.example.chatwithai.domain.use_case.messages.UseMessage
import com.example.chatwithai.domain.util.ItemsOrder
import com.example.chatwithai.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val messageUseCases: MessageUseCases,
    private val useMessage: UseMessage
): ViewModel() {

    private val _state = mutableStateOf(MessagesState())
    val state: State<MessagesState> = _state

    private var recentlyDeletedMessage: MessageEntity? = null

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
            is MessageEvent.DeleteHistory -> {
                viewModelScope.launch {
                    messageUseCases.deleteAllMessages()
                }
            }
            is MessageEvent.DeleteMessage -> {
                viewModelScope.launch {
                    messageUseCases.deleteMessage(event.message)
                    recentlyDeletedMessage = event.message
                }
            }
            is MessageEvent.RestoreMessage -> {
                viewModelScope.launch {
                    messageUseCases.addMessage(recentlyDeletedMessage ?: return@launch)
                    recentlyDeletedMessage = null
                }
            }
            is MessageEvent.UpdateMessage -> {
                viewModelScope.launch {
                    messageUseCases.updateMessage(event.message.copy(
                        isStarred = !event.message.isStarred
                    ))
                }
            }
            is MessageEvent.ChangeStarredListVisibility -> {
                _state.value = state.value.copy(
                    areStarredChosen = !state.value.areStarredChosen
                )
                getMessages(state.value.itemsOrder)
            }
        }
    }

    fun onSharedEvent(event: MessageSharedEvent) {
        when (event) {
            is MessageSharedEvent.UseMessage -> {
                viewModelScope.launch {
                    Log.d("HistoryViewModel", "Send request with use case: ${event.message.request}")
                    useMessage.sendEvent(event)
                }
            }
        }
    }

    private fun getMessages(itemsOrder: ItemsOrder) {
        getMessagesJob?.cancel()
        getMessagesJob =
            when (state.value.areStarredChosen) {
                false -> {
                    messageUseCases.getMessages(itemsOrder).onEach { messages ->
                        _state.value = state.value.copy(
                            messages = messages,
                            itemsOrder = itemsOrder
                        )
                    }.launchIn(viewModelScope)
                }
                true -> {
                    messageUseCases.getStarredMessages(itemsOrder).onEach { messages ->
                        _state.value = state.value.copy(
                            messages = messages,
                            itemsOrder = itemsOrder
                        )
                    }.launchIn(viewModelScope)
                }
            }
    }
}