package com.example.chatwithai.presentation.rags

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.use_case.RagUseCases
import com.example.chatwithai.domain.util.OrderType
import com.example.chatwithai.domain.util.RagOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RagViewModel @Inject constructor(
    private val ragUseCases: RagUseCases
) : ViewModel() {

    private val _state = mutableStateOf(RagsState())
    val state: State<RagsState> = _state

    private var recentlyDeletedRag: Rag? = null

    private var getRagsJob: Job? = null

    init {
        getRags(RagOrder.Title(OrderType.Descending))
    }

    fun onEvent(event: RagsEvent) {
        when (event) {
            is RagsEvent.Order -> {
                if (state.value.ragOrder::class == event.ragOrder::class &&   // if chosen the same order as it's on screen
                    state.value.ragOrder.orderType == event.ragOrder.orderType) {
                    return
                }
                getRags(event.ragOrder)
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

    private fun getRags(ragOrder: RagOrder) {
        getRagsJob?.cancel()
        getRagsJob = ragUseCases.getRags(ragOrder).onEach { rags ->
            _state.value = state.value.copy(
                rags = rags,
                ragOrder = ragOrder
            )
        }.launchIn(viewModelScope)
    }
}