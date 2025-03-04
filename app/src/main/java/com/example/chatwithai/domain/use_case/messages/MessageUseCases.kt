package com.example.chatwithai.domain.use_case.messages

data class MessageUseCases(
    val deleteMessage: DeleteMessage,
    val deleteAllMessages: DeleteAllMessages,
    val getMessages: GetMessages,
    val getStarredMessages: GetStarredMessages,
    val getMessage: GetMessage,
    val addMessage: AddMessage,
    val updateMessage: UpdateMessage
)