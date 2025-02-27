package com.example.chatwithai.presentation.add_edit_rag

import androidx.compose.ui.focus.FocusState

sealed class AddEditRagEvent {
    data class EnteredTitle(val value: String): AddEditRagEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditRagEvent() // need to hide hint when user focuses title textfield
    data class EnteredContent(val value: String): AddEditRagEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditRagEvent()
    object SaveRag: AddEditRagEvent()
}