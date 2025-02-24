package com.example.chatwithai.presentation.chat_screen


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatwithai.domain.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()
    var userMessage by remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState()
    val state by viewModel.state.collectAsState() // loading state

    // Состояние списка
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Список сообщений
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            state = listState // Передаем состояние списка
        ) {
            items(messages) { message ->
                ShowMessage(message = message)
            }
        }

        // Прокрутка вниз при добавлении нового сообщения
        LaunchedEffect(messages) {
            if (messages.size > 0) {
                // Прокручиваем до конца списка
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        // Поле ввода сообщения
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = userMessage,
                onValueChange = {
                    if (!state.isLoading) userMessage = it
                }, // change only if it's not loading
                modifier = Modifier
                    .weight(1f),
                enabled = !state.isLoading  // block if it's loading
            )

            Button(
                onClick = {
                    if (userMessage.isNotBlank() && !state.isLoading) {
                        viewModel.sendMessage(userMessage)
                        userMessage = ""
                    }
                },
                modifier = Modifier
                    .padding(start = 8.dp),
                enabled = !state.isLoading
            ) {
                Text("Отправить")
            }
        }
        // Loading indicator
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Загрузка...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun ShowMessage(message: Message) {
    val alignment = if (message.isRequest) Alignment.TopEnd else Alignment.TopStart
    val color = if (message.isRequest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = color,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = message.text,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}