package com.example.chatwithai.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.common.Resource
import com.example.chatwithai.domain.model.Message
import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.model.Response
import com.example.chatwithai.domain.use_case.RequestUseCase
import com.example.chatwithai.domain.use_case.messages.SaveMessage
import com.example.chatwithai.domain.use_case.messages.UseMessage
import com.example.chatwithai.domain.use_case.rags.UseRag
import com.example.chatwithai.presentation.history.MessageSharedEvent
import com.example.chatwithai.presentation.rags.RagSharedEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val requestUseCase: RequestUseCase,
    private val ragEventUseCase: UseRag,
    private val saveMessageUseCase: SaveMessage,
    private val messageEventUseCase: UseMessage
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val _state = MutableStateFlow(ChatListState())
    val state: MutableStateFlow<ChatListState> = _state

    private val _userMessage = MutableStateFlow("")
    val userMessage: StateFlow<String> = _userMessage

    init {
        viewModelScope.launch {
            ragEventUseCase.observeEvents().collect { event ->
                event?.let {
                    handleRagEvent(it)
                }
            }
        }
        viewModelScope.launch {
            messageEventUseCase.observeEvents().collect{ event ->
                event?.let {
                    handleMessageEvent(it)
                }
            }
        }
    }

    fun handleRagEvent(event: RagSharedEvent) {
        when (event) {
            is RagSharedEvent.UseRag -> {   // adding in user request rag's message
                Log.d("ChatScreen", "Received use rag event. Added in request: ${event.rag.content}")
                Log.d("ChatScreen", "Usermessage: ${userMessage.value}")
                var newText = ""
                // should never happen, but check
                if (event.rag.content.isNotBlank()) {
                    newText = userMessage.value + " " + event.rag.content
                } else newText = userMessage.value
                updateUserMessage(newText)
            }
        }
        clearEvent(1) // clear event after handling
    }

    fun handleMessageEvent(event: MessageSharedEvent) {
        when (event) {
            is MessageSharedEvent.UseMessage -> {
                Log.d("ChatScreen", "Received use message event. Added in request: ${event.message.request}")
                Log.d("ChatScreen", "Usermessage: ${userMessage.value}")
                var newText = ""
                if (event.message.request.isNotBlank()) {
                    newText = userMessage.value + " " + event.message.request
                } else newText = userMessage.value
                updateUserMessage(newText)
            }
        }
        clearEvent(2) // clear event after handling
    }

    fun updateUserMessage(newText: String) {
        _userMessage.value = newText
    }

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

                            // adding successful request in messages history
                            saveMessageUseCase(
                                MessageEntity(   // null id means create new message in db
                                    request = message,
                                    response = response.response,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                        }

                        is Resource.Error -> {
                            _messages.value += Message(
                                text = result.message ?: "An unexpected error occured",
                                isRequest = false
                            )

                            saveMessageUseCase(
                                MessageEntity(   // null id means create new message in db
                                    request = message,
                                    response = result.message ?: "",
                                    timestamp = System.currentTimeMillis()
                                )
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

    fun clearEvent(flag: Int) {  // 1 = rag event, 2 = message event
        when (flag) {
            1 -> {
                viewModelScope.launch {
                    ragEventUseCase.clearEvent()
                }
            }
            2 -> {
                viewModelScope.launch {
                    messageEventUseCase.clearEvent()
                }
            }
        }
    }
}