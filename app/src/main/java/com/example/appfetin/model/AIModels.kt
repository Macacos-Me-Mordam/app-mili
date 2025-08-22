package com.example.appfetin.model

import com.google.gson.annotations.SerializedName

data class AIRequest(
    @SerializedName("model") val model: String = "sabia-3",
    @SerializedName("messages") val messages: List<AIMessage>,
    @SerializedName("max_tokens") val maxTokens: Int = 4000
)

data class AIMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class AIResponse(
    @SerializedName("choices") val choices: List<AIChoice>
)

data class AIChoice(
    @SerializedName("message") val message: AIMessage
)