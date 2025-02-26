package com.example.chatwithai.domain.use_case

data class RagUseCases(
    val getRags: GetRags,
    val getRag: GetRag,
    val deleteRag: DeleteRag,
    val addRag: AddRag
)