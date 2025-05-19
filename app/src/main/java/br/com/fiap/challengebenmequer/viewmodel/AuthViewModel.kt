package br.com.fiap.challengebenmequer.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.challengebenmequer.data.repository.SessionRepository
import br.com.fiap.challengebenmequer.dto.AuthRequestDTO
import br.com.fiap.challengebenmequer.dto.UsuarioMeDTO
import br.com.fiap.challengebenmequer.network.RetrofitClient
import kotlinx.coroutines.launch // Para executar operações de rede em background

class AuthViewModel : ViewModel() {

    // Estados observáveis para a UI
    // Usamos mutableStateOf para que a UI do Compose possa reagir a mudanças nesses valores.
    val apiResponseMessage = mutableStateOf<String?>(null) // Para exibir mensagens genéricas de sucesso/erro da APIara armazenar o token após o login
    val loggedInToken: State<String?> = SessionRepository.authToken
    val userProfile = mutableStateOf<UsuarioMeDTO?>(null) // Para armazenar os dados do perfil do usuário
    val isLoading = mutableStateOf(false)                 // Para indicar se uma operação de rede está em andamento

    // Instância do nosso serviço de API
    private val apiService = RetrofitClient.instance

    // Função para simular o registro de usuário
    fun registerUser(password: String) {
        isLoading.value = true
        apiResponseMessage.value = null // Limpa mensagem anterior
        userProfile.value = null      // Limpa perfil anterior

        // O username é gerado no backend mockado, então não o enviamos como parte do DTO de registro.
        val request = AuthRequestDTO(username = null, password = password)

        viewModelScope.launch {
            try {
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    SessionRepository.saveToken(authResponse?.token) // SALVA O TOKEN
                    apiResponseMessage.value = "Registro mockado com sucesso! Token: ${authResponse?.token}"
                    Log.d("AuthViewModel", "Token de registro salvo: ${SessionRepository.getToken()}")
                } else {
                    // ... tratamento de erro ...
                }
            } catch (e: Exception) {
                // ... tratamento de erro ...
            } finally {
                isLoading.value = false
            }
        }
    }

    // Função para simular o login de usuário
    fun loginUser(username: String, password: String) {
        isLoading.value = true
        apiResponseMessage.value = null
        SessionRepository.clearToken() // Limpa token antigo antes de tentar novo login

        val request = AuthRequestDTO(username = username, password = password)

        viewModelScope.launch {
            try {
                val response = apiService.login(request)
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    SessionRepository.saveToken(authResponse?.token) // SALVA O TOKEN
                    apiResponseMessage.value = "Login mockado com sucesso!"
                    Log.d("AuthViewModel", "Token de login salvo: ${SessionRepository.getToken()}")
                } else {
                    apiResponseMessage.value = "Falha no login: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (login): ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearApiResponseMessage() {
        apiResponseMessage.value = null
    }
}