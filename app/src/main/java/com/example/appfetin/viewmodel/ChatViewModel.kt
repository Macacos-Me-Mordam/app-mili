package com.example.appfetin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfetin.model.AIMessage
import com.example.appfetin.model.ChatMessage
import com.example.appfetin.repository.AIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val aiRepository = AIRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val conversationHistory = mutableListOf<AIMessage>()

    init {
        addBotMessage("Olá! Sou o MiliBot, seu assistente para questões ambientais. Como posso ajudar você hoje?")
    }

    fun sendMessage(userMessageText: String) {
        if (userMessageText.isBlank()) return

        addUserMessage(userMessageText)

        conversationHistory.add(AIMessage(role = "user", content = userMessageText))

        getAIResponse()
    }

    private fun getAIResponse() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = aiRepository.getAIResponse(
                userMessage = conversationHistory.last().content,
                conversationHistory = conversationHistory.dropLast(1) 
            )

            result.fold(
                onSuccess = { aiResponse ->
                    addBotMessage(aiResponse)
                    conversationHistory.add(AIMessage(role = "assistant", content = aiResponse))
                },
                onFailure = { exception ->
                    val errorMessage = "Erro ao conectar com a IA: ${exception.message}"
                    _error.value = errorMessage
                    addBotMessage("Desculpe, estou com problemas para me conectar. Tente novamente mais tarde.")
                }
            )

            _isLoading.value = false
        }
    }

    private fun addUserMessage(content: String) {
        val message = ChatMessage(
            id = System.currentTimeMillis().toString(),
            senderName = "Você",
            senderAvatar = "👤",
            scope = null,
            content = content,
            isFromUser = true
        )
        _messages.value += message
    }

    private fun addBotMessage(content: String) {
        val message = ChatMessage(
            id = System.currentTimeMillis().toString(),
            senderName = "MiliBot",
            senderAvatar = "🤖",
            scope = "Meio ambiente",
            content = content,
            isFromUser = false
        )
        _messages.value += message
    }

    fun clearError() {
        _error.value = null
    }
    
    fun testConnection() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = aiRepository.testConnection()
                result.fold(
                    onSuccess = { response ->
                        _error.value = "✅ $response"
                    },
                    onFailure = { exception ->
                        _error.value = "❌ Erro na conexão: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _error.value = "❌ Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}