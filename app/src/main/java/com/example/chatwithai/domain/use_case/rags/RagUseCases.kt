package com.example.chatwithai.domain.use_case.rags

data class RagUseCases(
    val getRags: GetRags,
    val getRag: GetRag,
    val deleteRag: DeleteRag,
    val addRag: AddRag,
    val updateRag: UpdateRag,
    val getStarredRags: GetStarredRags
)