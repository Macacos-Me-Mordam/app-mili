package com.example.appfetin.util

import android.content.Context
import android.content.SharedPreferences

class RateLimiter(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("rate_limiter_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val REQUEST_TIMESTAMPS_KEY = "request_timestamps"
        private const val MAX_REQUESTS = 50 // Limite de 50 requisições
        private val TIME_WINDOW_MS = 60 * 60 * 1000L // Janela de 1 hora em milissegundos
    }

    /**
     * Verifica se o limite de requisições foi atingido.
     * Esta função também limpa timestamps antigos.
     * @return `true` se o limite foi atingido, `false` caso contrário.
     */
    fun isRateLimited(): Boolean {
        val currentTime = System.currentTimeMillis()
        val validTimestamps = getValidTimestamps(currentTime)

        // Salva a lista limpa de volta no SharedPreferences
        saveTimestamps(validTimestamps)

        return validTimestamps.size >= MAX_REQUESTS
    }

    /**
     * Registra o timestamp de uma nova requisição.
     */
    fun recordRequestTimestamp() {
        val currentTime = System.currentTimeMillis()
        val validTimestamps = getValidTimestamps(currentTime).toMutableList()
        validTimestamps.add(currentTime)
        saveTimestamps(validTimestamps)
    }

    private fun getTimestamps(): List<Long> {
        val timestampsString = prefs.getString(REQUEST_TIMESTAMPS_KEY, null) ?: return emptyList()
        return timestampsString.split(",").filter { it.isNotBlank() }.map { it.toLong() }
    }

    private fun getValidTimestamps(currentTime: Long): List<Long> {
        val allTimestamps = getTimestamps()
        val windowStartTime = currentTime - TIME_WINDOW_MS
        return allTimestamps.filter { it >= windowStartTime }
    }

    private fun saveTimestamps(timestamps: List<Long>) {
        val timestampsString = timestamps.joinToString(",")
        prefs.edit().putString(REQUEST_TIMESTAMPS_KEY, timestampsString).apply()
    }
}