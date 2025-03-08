package com.example.chatwithai.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.common.Resource
import com.example.chatwithai.domain.model.Chat
import com.example.chatwithai.domain.model.Message
import com.example.chatwithai.domain.model.MessageEntity
import com.example.chatwithai.domain.model.Response
import com.example.chatwithai.domain.use_case.RequestUseCase
import com.example.chatwithai.domain.use_case.chats.ChatUseCases
import com.example.chatwithai.domain.use_case.messages.GetMessagesByChatId
import com.example.chatwithai.domain.use_case.messages.SaveMessage
import com.example.chatwithai.domain.use_case.messages.UseMessage
import com.example.chatwithai.domain.use_case.rags.UseRag
import com.example.chatwithai.presentation.chat.components.MenuItem
import com.example.chatwithai.presentation.history.MessageSharedEvent
import com.example.chatwithai.presentation.rags.RagSharedEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val requestUseCase: RequestUseCase,
    private val ragEventUseCase: UseRag,
    private val saveMessageUseCase: SaveMessage,
    private val messageEventUseCase: UseMessage,
    private val chatUseCases: ChatUseCases,
    private val getMessagesByChatId: GetMessagesByChatId
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val _state = MutableStateFlow(ChatListState())
    val state: MutableStateFlow<ChatListState> = _state

    private val _userMessage = MutableStateFlow("")
    val userMessage: StateFlow<String> = _userMessage

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _chosenChat = MutableStateFlow<Int>(1)
    val chosenChat: StateFlow<Int> = _chosenChat

    private var getChatsJob: Job? = null
    private var getHistoryJob: Job? = null

    init {
        getChats()
        getChatHistory(1)

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

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.ChangeChat -> {
                _chosenChat.value = event.chatId ?: 1
                getChatHistory(event.chatId ?: 1)
            }
            is ChatEvent.AddChat -> {
                viewModelScope.launch {
                    chatUseCases.addChat(event.chat)

                    // get last added chat
                    val lastChat = chatUseCases.getLastChat()
                    lastChat?.let { chat ->
                        _chosenChat.value = chat.id ?: 1
                        getChatHistory(chat.id ?: 1)
                    }
                }
            }
            is ChatEvent.DeleteChat -> {
                viewModelScope.launch {
                    chatUseCases.deleteChat(event.chat)
                }
                _chosenChat.value = 1
                getChatHistory(1)
            }
            is ChatEvent.UpdateChat -> {
                viewModelScope.launch {
                    chatUseCases.updateChat(event.chat)
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
                                    timestamp = System.currentTimeMillis(),
                                    chatId = chosenChat.value
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
                                    timestamp = System.currentTimeMillis(),
                                    chatId = chosenChat.value
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

    fun getChats() {
        getChatsJob?.cancel()
        getChatsJob =
            chatUseCases.getChats().onEach { getChats ->
                _chats.value = getChats
            }.launchIn(viewModelScope)
    }

    fun getChatHistory(chatId: Int) {
        getHistoryJob?.cancel()
        getHistoryJob =
        getMessagesByChatId(chatId).map { messageEntities ->
            // convert MessageEntity in 2 Messages
            messageEntities.flatMap { messageEntity ->
                listOf(
                    Message(
                        text = messageEntity.request,
                        isRequest = true
                    ),
                    Message(
                        text = messageEntity.response,
                        isRequest = false
                    )
                )
            }
        }.onEach { messages ->
            _messages.value = messages
        }.launchIn(viewModelScope)
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