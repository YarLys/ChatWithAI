package com.example.chatwithai.presentation.rags

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatwithai.presentation.components.SearchBar
import com.example.chatwithai.presentation.rags.components.OrderSection
import com.example.chatwithai.presentation.rags.components.RagItem
import com.example.chatwithai.presentation.util.Screen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RagScreen(
    navController: NavController
) {
    val viewModel: RagViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val searchText by viewModel.searchText.collectAsState()
    val filteredRags = viewModel.filteredRags.value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditRagScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add rag")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    searchText = searchText,
                    onSearchTextChanged = { viewModel.onEvent(RagsEvent.onSearchTextChanged(it)) }
                )
                IconButton(
                    onClick = {
                        viewModel.onEvent(RagsEvent.ChangeStarredListVisibility)
                    }
                ) {
                    Icon(
                        imageVector = if (!state.areStarredChosen) Icons.Outlined.StarOutline else Icons.Default.Star,
                        contentDescription = "StarredMessages",
                        tint = if (state.areStarredChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.onEvent(RagsEvent.ToggleOrderSection)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    itemsOrder = state.itemsOrder,
                    onOrderChange = {
                        viewModel.onEvent(RagsEvent.Order(it))  // order has changed
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredRags) { rag ->
                    RagItem(
                        rag = rag,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditRagScreen.route +
                                            "?ragId=${rag.id}"
                                ) // navigate to edit chosen rag
                            },
                        onDeleteClick = {
                            viewModel.onEvent(RagsEvent.DeleteRag(rag))
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val result = snackbarHostState.showSnackbar(
                                    message = "RAG удален",
                                    actionLabel = "Отмена"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(RagsEvent.RestoreRag)
                                }
                            }
                        },
                        onUseClick = {
                            Log.d("RagScreen", "Send use rag event: ${rag.content}")
                            viewModel.onSharedEvent(RagSharedEvent.UseRag(rag))
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = "RAG добавлен в запрос"
                                )
                            }
                        },
                        onStarClick = {
                            viewModel.onEvent(RagsEvent.UpdateRag(rag))
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = if (rag.isStarred) "RAG удалён из избранного" else "RAG добавлен в избранное"
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}