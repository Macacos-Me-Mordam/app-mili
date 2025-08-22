package com.example.appfetin.network

import com.example.appfetin.model.AIRequest
import com.example.appfetin.model.AIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AIService {
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Body request: AIRequest
    ): AIResponse
}