package com.example.appfetin.repository

import com.example.appfetin.model.AIMessage
import com.example.appfetin.model.AIRequest
import com.example.appfetin.network.AIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AIRepository {

    private val apiKey = "105629259555388293248_2e2248084077ed30"

    private val aiService: AIService by lazy {
        createAIService()
    }

    private fun createAIService(): AIService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-Agent", "AppFetin/1.0")
                .build()
            
            val response = chain.proceed(newRequest)
            
            println("Status Code: ${response.code}")
            println("Response Headers: ${response.headers}")
            
            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                println("Error Body: $errorBody")
                
                when (response.code) {
                    401 -> throw Exception("API Key inválida ou expirada")
                    403 -> throw Exception("Acesso negado. Verifique sua API Key e permissões")
                    429 -> throw Exception("Limite de requisições excedido")
                    500 -> throw Exception("Erro interno do servidor da IA")
                    else -> throw Exception("Erro HTTP ${response.code}: ${response.message}")
                }
            }
            
            response
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor) 
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://chat.maritaca.ai/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AIService::class.java)
    }

    suspend fun getAIResponse(
        userMessage: String,
        conversationHistory: List<AIMessage>
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val messages = mutableListOf<AIMessage>()
            
            messages.add(
                AIMessage(
                    role = "system",
                    content = "Você é o Mili Ai, um assistente virtual especializado em questões ambientais e sustentabilidade.\n" +
                            "\n" +
                            "Sua única missão e especialidade é responder a perguntas sobre:\n" +
                            "- Reciclagem e tipos de materiais recicláveis.\n" +
                            "- Descarte correto de resíduos (lixo eletrônico, pilhas, óleo de cozinha, etc.).\n" +
                            "- Riscos e impacto do descarte incorreto.\n" +
                            "- Conscientização e dicas de sustentabilidade.\n" +
                            "\n" +
                            "Siga estas regras:\n" +
                            "1. Seja amigável, informativo e direto.\n" +
                            "2. Mantenha as respostas claras, acessíveis e, acima de tudo, breves.\n" +
                            "3. Use o português brasileiro.\n" +
                            "\n" +
                            "INSTRUÇÃO CRUCIAL:\n" +
                            "Se a pergunta do usuário **não estiver relacionada** com o tema de meio ambiente ou sustentabilidade, **você deve responder exatamente com a seguinte frase**:\n" +
                            "\n" +
                            "\"Desculpe, não fui criado para responder a perguntas que não sejam sobre meio ambiente e sustentabilidade.\""
                )
            )
            
            messages.addAll(conversationHistory)
            
            messages.add(
                AIMessage(role = "user", content = userMessage)
            )

            val request = AIRequest(
                model = "sabiazinho-3",
                messages = messages,
                maxTokens = 4000
            )

            println("Enviando requisição para IA...")
            println("Mensagens: ${messages.size}")
            println("API Key (primeiros 10 chars): ${apiKey.take(10)}...")

            val response = aiService.getChatCompletion(request)

            val aiResponse = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("Resposta vazia ou em formato inesperado da IA")

            println("Resposta da IA recebida com sucesso")
            Result.success(aiResponse)

        } catch (e: Exception) {
            println("Erro na requisição da IA: ${e.message}")
            println("Tipo do erro: ${e.javaClass.simpleName}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        try {
            println("Testando conexão com a API Maritaca...")
            
            val testMessages = listOf(
                AIMessage(role = "user", content = "Teste de conexão")
            )
            
            val request = AIRequest(
                model = "sabiazinho-3",
                messages = testMessages,
                maxTokens = 50
            )
            
            val response = aiService.getChatCompletion(request)
            val content = response.choices.firstOrNull()?.message?.content
            
            if (content != null) {
                Result.success("Conexão bem-sucedida! Resposta: $content")
            } else {
                Result.failure(Exception("Resposta vazia da API"))
            }
            
        } catch (e: Exception) {
            println("Erro no teste de conexão: ${e.message}")
            Result.failure(e)
        }
    }
    
}