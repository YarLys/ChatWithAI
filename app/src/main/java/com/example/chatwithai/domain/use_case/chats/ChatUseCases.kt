package com.example.chatwithai.domain.use_case.chats

data class ChatUseCases(
    val getChats: GetChats,
    val addChat: AddChat,
    val deleteChat: DeleteChat,
    val updateChat: UpdateChat,
    val getLastChat: GetLastChat
)
