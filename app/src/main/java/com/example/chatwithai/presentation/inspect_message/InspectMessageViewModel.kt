package com.example.chatwithai.presentation.inspect_message

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.domain.use_case.messages.MessageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectMessageViewModel @Inject constructor(
    private val messageUseCases: MessageUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _request = mutableStateOf("")
    val request: State<String> = _request

    private val _response = mutableStateOf("")
    val response: State<String> = _response

    init {
        savedStateHandle.get<Int>("messageId")?.let { messageId ->
            viewModelScope.launch {
                messageUseCases.getMessage(messageId)?.also { message ->
                    _request.value = message.request
                    _response.value = message.response
                }
            }
        }
    }
}