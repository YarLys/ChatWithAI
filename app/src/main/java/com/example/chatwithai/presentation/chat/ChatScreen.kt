package com.example.chatwithai.presentation.chat


import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatwithai.common.Resource
import com.example.chatwithai.domain.model.Chat
import com.example.chatwithai.domain.model.Message
import com.example.chatwithai.presentation.chat.components.AppBar
import com.example.chatwithai.presentation.chat.components.DrawerBody
import com.example.chatwithai.presentation.chat.components.DrawerHeader
import com.example.chatwithai.presentation.chat.components.MenuItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()
    val userMessage by viewModel.userMessage.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val state by viewModel.state.collectAsState() // loading state

    // list state
    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val chats by viewModel.chats.collectAsState()  // list of chats
    val chosenChat by viewModel.chosenChat.collectAsState()

    val exportStatus by viewModel.exportStatus.collectAsState()
    // Launcher for Scoped Storage
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain"),
        onResult = { uri ->
            if (uri != null) {
                viewModel.exportData(uri)
            }
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                DrawerHeader(
                    onAddClick = {
                        viewModel.onEvent(ChatEvent.AddChat(Chat(title = it)))
                    }
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    DrawerBody(
                        items = chats.map { chat ->
                            MenuItem(
                                id = chat.id,
                                title = chat.title,
                                icon = Icons.Default.Chat,
                                isSelected = chat.id == chosenChat
                            )
                        },
                        onItemClick = { chat ->
                            viewModel.onEvent(ChatEvent.ChangeChat(chat.id))
                            scope.launch {
                                drawerState.close()  // close drawer when select chat
                            }
                        },
                        onDeleteClick = { chat ->
                            viewModel.onEvent(ChatEvent.DeleteChat(Chat(chat.title, chat.id)))
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = "Чат удален"
                                )
                            }
                        },
                        onEditClick = { chat ->
                            viewModel.onEvent(ChatEvent.UpdateChat(Chat(chat.title, chat.id)))
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = "Чат переименован"
                                )
                            }
                        }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.End
                    ) {
                        Divider()
                        IconButton(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    createFileLauncher.launch("chat_export.txt")
                                } else {
                                    viewModel.exportData(null)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = "ExportDataToFile"
                            )
                        }
                    }
                }
            }
        },
        gesturesEnabled = true, // allow swipe to open/close Drawer
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // messages list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp),
                    state = listState // set list state
                ) {
                    items(messages) { message ->
                        ShowMessage(message = message)
                    }
                }

                // scroll down when adding new message
                LaunchedEffect(messages) {
                    if (messages.size > 0) {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = userMessage,
                        onValueChange = {
                            if (!state.isLoading) {
                                viewModel.updateUserMessage(it)
                            }
                        }, // change only if it's not loading
                        modifier = Modifier
                            .weight(1f),
                        enabled = !state.isLoading  // block if it's loading
                    )

                    Button(
                        onClick = {
                            if (userMessage.isNotBlank() && !state.isLoading) {
                                viewModel.sendMessage(userMessage)
                                viewModel.updateUserMessage("") // clear text after sending
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
    }
    // check export status
    LaunchedEffect(exportStatus) {
        when (exportStatus) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Данные сохранены в chat_export.txt"
                    )
                }
            }

            is Resource.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Не удалось сохранить данные в файл"
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
fun ShowMessage(message: Message) {
    val alignment = if (message.isRequest) Alignment.TopEnd else Alignment.TopStart
    val color =
        if (message.isRequest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

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