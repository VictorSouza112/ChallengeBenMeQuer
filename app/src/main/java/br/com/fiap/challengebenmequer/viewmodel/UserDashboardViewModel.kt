package br.com.fiap.challengebenmequer.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.challengebenmequer.data.repository.SessionRepository
import br.com.fiap.challengebenmequer.dto.CheckinHistoryItemDTO
import br.com.fiap.challengebenmequer.dto.CheckinSummaryDTO
import br.com.fiap.challengebenmequer.dto.UsuarioMeDTO
import br.com.fiap.challengebenmequer.network.RetrofitClient
import kotlinx.coroutines.launch

class UserDashboardViewModel : ViewModel() {

    // Estados observáveis para a UI
    // Usamos mutableStateOf para que a UI do Compose possa reagir a mudanças nesses valores.
    val apiResponseMessage = mutableStateOf<String?>(null)
    val userProfile = mutableStateOf<UsuarioMeDTO?>(null) // Se esta tela for buscar perfil
    val isLoading = mutableStateOf(false)
    val checkinHistory = mutableStateOf<List<CheckinHistoryItemDTO>?>(null)
    val checkinSummary = mutableStateOf<CheckinSummaryDTO?>(null)

    // Instância do nosso serviço de API
    private val apiService = RetrofitClient.instance

    // Função para buscar o perfil do usuário
    fun fetchUserProfile() {
        val token = SessionRepository.getToken()
        if (token == null) {
            apiResponseMessage.value = "Usuário não logado (perfil)."
            return
        }

        isLoading.value = true
        apiResponseMessage.value = null
        userProfile.value = null

        viewModelScope.launch {
            try {
                // O token JWT real deve ser enviado com o prefixo "Bearer "
                val authHeader = "Bearer $token"
                val response = apiService.getMyProfile(authHeader)
                if (response.isSuccessful) {
                    userProfile.value = response.body()
                    apiResponseMessage.value = "Perfil do usuário carregado."
                } else {
                    apiResponseMessage.value = "Falha ao buscar perfil: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (perfil): ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchCheckinHistory() {
        val token = SessionRepository.getToken()
        if (token == null) {
            apiResponseMessage.value = "Usuário não logado (histórico)."
            checkinHistory.value = null
            return
        }

        isLoading.value = true
        apiResponseMessage.value = null // Limpa mensagens de status anteriores
        checkinHistory.value = null   // Limpa o valor do histórico anterior antes de buscar um novo

        viewModelScope.launch { // Executa a chamada de rede em uma coroutine (background thread)
            try {
                // Prepara o cabeçalho de autorização. A API espera "Bearer <seu_token>"
                val authHeader = "Bearer $token"
                // Chama o método da interface ApiService (que será definido na próxima etapa)
                val response = apiService.getCheckinHistory(authHeader)

                if (response.isSuccessful) {
                    checkinHistory.value = response.body() // Armazena a lista de DTOs recebida
                    if (checkinHistory.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum histórico de check-in encontrado."
                    } else {
                        apiResponseMessage.value = "Histórico de check-ins carregado com sucesso."
                    }
                } else {
                    // Se a resposta não for bem-sucedida (ex: erro 4xx, 5xx)
                    apiResponseMessage.value = "Falha ao buscar histórico: ${response.code()} - ${response.errorBody()?.string()}"
                    checkinHistory.value = null
                }
            } catch (e: Exception) {
                // Captura exceções de rede (ex: sem conexão, timeout) ou outras
                apiResponseMessage.value = "Erro de rede ao buscar histórico de check-ins: ${e.message}"
                checkinHistory.value = null
                Log.e("AuthViewModel", "Erro em fetchCheckinHistory: ", e) // Loga a exceção para debug
            } finally {
                isLoading.value = false // Garante que o indicador de loading seja desativado
            }
        }
    }

    fun fetchCheckinSummary(period: String = "last7days") {
        val token = SessionRepository.getToken()
        if (token == null) {
            apiResponseMessage.value = "Usuário não logado (resumo)."
            checkinSummary.value = null
            return
        }

        isLoading.value = true
        apiResponseMessage.value = null
        checkinSummary.value = null // Limpa resumo anterior

        viewModelScope.launch {
            try {
                val authHeader = "Bearer $token"
                // Chama o novo método da ApiService, passando o período
                val response = apiService.getCheckinSummary(authHeader, period)

                if (response.isSuccessful) {
                    checkinSummary.value = response.body()
                    apiResponseMessage.value = "Resumo de check-ins carregado."
                } else {
                    apiResponseMessage.value = "Falha ao buscar resumo: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (resumo de check-ins): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchCheckinSummary: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }
}