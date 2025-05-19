// Arquivo: data/repository/SessionRepository.kt
package br.com.fiap.challengebenmequer.data.repository // Ou o seu pacote

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

object SessionRepository {
    private val _authToken = mutableStateOf<String?>(null)
    val authToken: State<String?> = _authToken

    fun saveToken(token: String?) {
        _authToken.value = token
    }

    fun getToken(): String? {
        return _authToken.value
    }

    fun clearToken() {
        _authToken.value = null
    }
}