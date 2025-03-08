package com.example.chatwithai.presentation.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerHeader(
    onAddClick: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var chatName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Сохраненные чаты",
                fontSize = 20.sp,
            )
            IconButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "AddChat",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false // close dialog when user clicks out of its space
            },
            title = {
                Text(text = "Новый чат", style = MaterialTheme.typography.headlineSmall)
            },
            text = {
                OutlinedTextField(
                    value = chatName,
                    onValueChange = { chatName = it },
                    label = { Text("Название чата") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAddClick(chatName)
                        showDialog = false
                        chatName = ""
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        chatName = ""
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit,
    onDeleteClick: (MenuItem) -> Unit,
    onEditClick: (MenuItem) -> Unit
) {
    var showRenameDialog by remember { mutableStateOf(false) }
    var chatToRename by remember { mutableStateOf<MenuItem?>(null) }
    var newChatName by remember { mutableStateOf("") }

    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = "chat"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
                if (item.id != 1) {   // 1 chat must be available
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                chatToRename = item
                                newChatName = item.title
                                showRenameDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "RenameChat"
                            )
                        }
                        IconButton(
                            onClick = { onDeleteClick(item) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "DeleteChat"
                            )
                        }
                    }
                }
            }
        }
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = {
                showRenameDialog = false
            },
            title = {
                Text(text = "Переименовать чат", style = MaterialTheme.typography.headlineSmall)
            },
            text = {
                OutlinedTextField(
                    value = newChatName,
                    onValueChange = { newChatName = it },
                    label = { Text("Новое название чата") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        chatToRename?.let { item ->
                            onEditClick(item.copy(title = newChatName))
                        }
                        showRenameDialog = false
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRenameDialog = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}