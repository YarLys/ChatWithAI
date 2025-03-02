package com.example.chatwithai.presentation.add_edit_rag

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithai.domain.model.InvalidRagException
import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.use_case.rags.RagUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRagViewModel @Inject constructor(
    private val ragUseCases: RagUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _ragTitle = mutableStateOf(RagTextFieldState(
        hint = "Введите тип шаблона"
    ))
    val ragTitle: State<RagTextFieldState> = _ragTitle

    private val _ragContent = mutableStateOf(RagTextFieldState(
        hint = "Введите текст шаблона"
    ))
    val ragContent: State<RagTextFieldState> = _ragContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentRagId: Int? = null

    init {
        savedStateHandle.get<Int>("ragId")?.let { ragId ->
            viewModelScope.launch {
                ragUseCases.getRag(ragId)?.also { rag ->
                    currentRagId = rag.id
                    _ragTitle.value = ragTitle.value.copy(
                        text = rag.title,
                        isHintVisible = false
                    )
                    _ragContent.value = ragContent.value.copy(
                        text = rag.content,
                        isHintVisible = false
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditRagEvent) {
        when (event) {
            is AddEditRagEvent.EnteredTitle -> {
                _ragTitle.value = ragTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditRagEvent.ChangeTitleFocus -> {
                _ragTitle.value = ragTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && ragTitle.value.text.isBlank()
                )
            }
            is AddEditRagEvent.EnteredContent -> {
                _ragContent.value = ragContent.value.copy(
                    text = event.value
                )
            }
            is AddEditRagEvent.ChangeContentFocus -> {
                _ragContent.value = ragContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && ragContent.value.text.isBlank()
                )
            }
            is AddEditRagEvent.SaveRag -> {
                viewModelScope.launch {
                    try {
                        ragUseCases.addRag(
                            Rag(
                                title = ragTitle.value.text,
                                content = ragContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                id = currentRagId  // if null -> create new note. else replace old
                            )
                        )
                        _eventFlow.emit(UiEvent.saveRag)
                    } catch (e: InvalidRagException) {
                        _eventFlow.emit(
                            UiEvent.showSnackBar(
                                message = e.message ?: "Не удалось сохранить шаблон"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class showSnackBar(val message: String): UiEvent()
        object saveRag: UiEvent()
    }
}