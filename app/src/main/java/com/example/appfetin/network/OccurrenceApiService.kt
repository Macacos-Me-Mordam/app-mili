package com.example.appfetin.network

import com.example.appfetin.model.LoginRequest
import com.example.appfetin.model.Occurrence
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OccurrenceApiService {
    @POST("/auth/login") // Substitua "login" pelo endpoint correto de login
    suspend fun login(@Body loginRequest: LoginRequest): Response<Unit> // Retorna Response<Unit>

    @POST("app-occurrence")
    suspend fun createOccurrence(
        @Body occurrence: Occurrence // O Header de autorização foi removido
    ): Response<Unit>
}