package com.example.chatwithai.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.common.Resource
import com.example.chatwithai.domain.model.Message
import com.example.chatwithai.domain.model.Response
import com.example.chatwithai.domain.use_case.RequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val requestUseCase: RequestUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val _state = MutableStateFlow(ChatListState())
    val state: MutableStateFlow<ChatListState> = _state

    fun sendMessage(message: String) {
        viewModelScope.launch {
            // Set loading's state in True
            _state.value = ChatListState(isLoading = true)

            // Add user's message in list
            _messages.value += Message(text = message, isRequest = true)
            try {
                // make request and get response, add it in list
                requestUseCase(message).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val response = result.data ?: Response("")
                            _messages.value += Message(text = response.response, isRequest = false)
                        }

                        is Resource.Error -> {
                            _messages.value += Message(
                                text = result.message ?: "An unexpected error occured",
                                isRequest = false
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = ChatListState(isLoading = true)
                        }
                    }
                }
            } finally {
                _state.value = ChatListState(isLoading = false)
            }
        }
    }
}