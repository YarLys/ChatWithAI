package com.example.chatwithai.presentation.rags

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.use_case.rags.RagUseCases
import com.example.chatwithai.domain.use_case.rags.UseRag
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.ItemsOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RagViewModel @Inject constructor(
    private val ragUseCases: RagUseCases,
    private val useRagUseCase: UseRag
) : ViewModel() {

    private val _state = mutableStateOf(RagsState())
    val state: State<RagsState> = _state

    private var recentlyDeletedRag: Rag? = null

    private var getRagsJob: Job? = null

    init {
        getRags(ItemsOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: RagsEvent) {
        when (event) {
            is RagsEvent.Order -> {
                if (state.value.itemsOrder::class == event.itemsOrder::class &&   // if chosen the same order as it's on screen
                    state.value.itemsOrder.orderType == event.itemsOrder.orderType) {
                    return
                }
                getRags(event.itemsOrder)
            }
            is RagsEvent.DeleteRag -> {
                viewModelScope.launch {
                    ragUseCases.deleteRag(event.rag)
                    recentlyDeletedRag = event.rag
                }
            }
            is RagsEvent.RestoreRag -> {
                viewModelScope.launch {
                    ragUseCases.addRag(recentlyDeletedRag ?: return@launch)
                    recentlyDeletedRag = null
                }
            }
            is RagsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    fun onSharedEvent(event: RagSharedEvent) {
        when (event) {
            is RagSharedEvent.UseRag -> {
                viewModelScope.launch {
                    Log.d("RagViewModel", "Receive use rag event and send with usecase. ${event.rag.content}")
                    useRagUseCase.sendEvent(event)
                }
            }
        }
    }

    private fun getRags(itemsOrder: ItemsOrder) {
        getRagsJob?.cancel()
        getRagsJob = ragUseCases.getRags(itemsOrder).onEach { rags ->
            _state.value = state.value.copy(
                rags = rags,
                itemsOrder = itemsOrder
            )
        }.launchIn(viewModelScope)
    }
}