package com.example.appfetin.model

data class ChatMessage(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val scope: String? = null,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatUser(
    val name: String,
    val avatar: String,
    val isBot: Boolean
)