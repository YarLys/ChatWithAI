package com.example.chatwithai.domain.use_case.messages

data class MessageUseCases(
    val getMessages: GetMessages,
    val deleteMessage: DeleteMessage,
    val deleteAllMessages: DeleteAllMessages,
    val getMessage: GetMessage,
    val addMessage: AddMessage,
    val updateMessage: UpdateMessage
)