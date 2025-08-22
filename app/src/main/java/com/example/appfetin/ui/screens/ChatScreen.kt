package com.example.appfetin.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appfetin.ui.theme.AppFetinTheme
import com.example.appfetin.ui.components.AppTopBar
import com.example.appfetin.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clearError()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                showNavigation = true,
                onBack = onNavigateBack,
                title = "Mili"
            )
        },
        bottomBar = {
            ChatInputSection(
                value = inputText,
                onValueChange = { inputText = it },
                onSendClick = {
                    if (inputText.isNotBlank() && !isLoading) {
                        viewModel.sendMessage(inputText.trim())
                        inputText = ""
                    }
                },
                isLoading = isLoading
            )
        },
        modifier = modifier
            ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            error?.let { errorMessage ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "⚠️ Erro",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }
                
                if (isLoading) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            TypingBubble()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: com.example.appfetin.model.ChatMessage,
    modifier: Modifier = Modifier
) {
    val alignment = if (message.isFromUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isFromUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }
    val textColor = if (message.isFromUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                    bottomEnd = if (message.isFromUser) 4.dp else 16.dp
                ),
                color = backgroundColor,
                modifier = Modifier.widthIn(max = 300.dp),
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    BubbleHeader(
                        senderAvatar = message.senderAvatar,
                        senderName = message.senderName,
                        scope = message.scope,
                        textColor = textColor,
                        isUser = message.isFromUser
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.content,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputSection(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isLoading: Boolean = false,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
                placeholder = {
                    Text(
                        text = if (isLoading) "Aguarde..." else "Digite sua mensagem...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )
            
            FloatingActionButton(
                onClick = onSendClick,
                modifier = Modifier.size(48.dp),
                containerColor = if (!isLoading && value.isNotBlank()) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.secondary,
                contentColor = if (!isLoading && value.isNotBlank())
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSecondary
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar mensagem"
                    )
                }
            }
        }
    }
}

@Composable
private fun BubbleHeader(
    senderAvatar: String,
    senderName: String,
    scope: String?,
    textColor: Color,
    isUser: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    if (isUser) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = senderAvatar, 
                fontSize = 12.sp, 
                color = textColor
            )
        }
        Text(
            text = senderName + (scope?.let { " • $it" } ?: ""),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@Composable
private fun TypingBubble() {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp),
        color = backgroundColor,
        modifier = Modifier.widthIn(max = 300.dp),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            BubbleHeader(
                senderAvatar = "🤖",
                senderName = "MiliBot",
                scope = "Meio ambiente",
                textColor = textColor,
                isUser = false
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Digitando...",
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    AppFetinTheme {
        ChatScreen(
            onNavigateBack = {}
        )
    }
}

