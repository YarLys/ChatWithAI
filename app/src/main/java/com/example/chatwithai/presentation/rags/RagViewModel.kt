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
import com.example.chatwithai.presentation.history.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RagViewModel @Inject constructor(
    private val ragUseCases: RagUseCases,
    private val useRagUseCase: UseRag
) : ViewModel() {

    private val _state = MutableStateFlow(RagsState())
    val state: StateFlow<RagsState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _filteredRags = mutableStateOf<List<Rag>>(emptyList())
    val filteredRags: State<List<Rag>> = _filteredRags

    private var recentlyDeletedRag: Rag? = null

    private var getRagsJob: Job? = null

    init {
        getRags(ItemsOrder.Date(OrderType.Descending))

        viewModelScope.launch {
            combine(_searchText,_state) { searchText, state ->
                if (searchText.isBlank()) {
                    state.rags
                }
                else {
                    state.rags.filter { rag ->
                        rag.title.contains(searchText, ignoreCase = true) ||
                        rag.content.contains(searchText, ignoreCase = true)
                    }
                }
            }.collect { filteredRags ->
                _filteredRags.value = filteredRags
            }
        }
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
            is RagsEvent.UpdateRag -> {
                viewModelScope.launch {
                    ragUseCases.updateRag(event.rag.copy(
                        isStarred = !event.rag.isStarred
                    ))
                }
            }
            is RagsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is RagsEvent.ChangeStarredListVisibility -> {
                _state.value = state.value.copy(
                    areStarredChosen = !state.value.areStarredChosen
                )
                getRags(state.value.itemsOrder)
            }
            is RagsEvent.onSearchTextChanged -> {
                _searchText.value = event.text
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
        getRagsJob =
            when (state.value.areStarredChosen) {
                false -> {
                    ragUseCases.getRags(itemsOrder).onEach { rags ->
                        _state.value = state.value.copy(
                            rags = rags,
                            itemsOrder = itemsOrder
                        )
                    }.launchIn(viewModelScope)
                }
                true -> {
                    ragUseCases.getStarredRags(itemsOrder).onEach { rags ->
                        _state.value = state.value.copy(
                            rags = rags,
                            itemsOrder = itemsOrder
                        )
                    }.launchIn(viewModelScope)
                }
            }
    }
}