package br.com.fiap.challengebenmequer.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.challengebenmequer.dto.AggregatedEmojiFeedbackDTO
import br.com.fiap.challengebenmequer.dto.AggregatedFeelingFeedbackDTO
import br.com.fiap.challengebenmequer.dto.AggregatedScaleQuestionDTO
import br.com.fiap.challengebenmequer.dto.AggregatedWorkloadQuestionDTO
import br.com.fiap.challengebenmequer.dto.WorkloadAnswerStatsItemDTO
import br.com.fiap.challengebenmequer.network.RetrofitClient
import kotlinx.coroutines.launch

class CompanyDataViewModel : ViewModel() {

    // Estados observáveis para a UI
    // Usamos mutableStateOf para que a UI do Compose possa reagir a mudanças nesses valores.
    val apiResponseMessage = mutableStateOf<String?>(null) // Para exibir mensagens genéricas de sucesso/erro da API
    val isLoading = mutableStateOf(false)                 // Para indicar se uma operação de rede está em andamento
    val aggregatedEmojiFeedback = mutableStateOf<List<AggregatedEmojiFeedbackDTO>?>(null)
    val aggregatedFeelingFeedback = mutableStateOf<List<AggregatedFeelingFeedbackDTO>?>(null)
    val aggregatedWorkloadFeedback = mutableStateOf<List<AggregatedWorkloadQuestionDTO>?>(null)
    val aggregatedAlertSignsFeedback = mutableStateOf<List<AggregatedWorkloadQuestionDTO>?>(null)
    val aggregatedClimateDiagnostics = mutableStateOf<List<AggregatedScaleQuestionDTO>?>(null)
    val aggregatedCommunicationDiagnostics = mutableStateOf<List<AggregatedScaleQuestionDTO>?>(null)
    val aggregatedLeadershipDiagnostics = mutableStateOf<List<AggregatedScaleQuestionDTO>?>(null)
    val selectedEmojiSliceForPopup = mutableStateOf<AggregatedEmojiFeedbackDTO?>(null)
    val selectedFeelingSliceForPopup = mutableStateOf<AggregatedFeelingFeedbackDTO?>(null)
    val selectedWorkloadSliceForPopup = mutableStateOf<WorkloadAnswerStatsItemDTO?>(null)
    val selectedAlertSignSliceForPopup = mutableStateOf<WorkloadAnswerStatsItemDTO?>(null)

    fun selectEmojiSlice(sliceData: AggregatedEmojiFeedbackDTO?) {
        selectedEmojiSliceForPopup.value = sliceData
    }

    fun selectFeelingSlice(sliceData: AggregatedFeelingFeedbackDTO?) {
        selectedFeelingSliceForPopup.value = sliceData
    }

    fun selectWorkloadSlice(sliceData: WorkloadAnswerStatsItemDTO?) {
        selectedWorkloadSliceForPopup.value = sliceData
    }

    fun selectAlertSignSlice(sliceData: WorkloadAnswerStatsItemDTO?) {
        selectedAlertSignSliceForPopup.value = sliceData
    }

    // Instância do nosso serviço de API
    private val apiService = RetrofitClient.instance

    fun fetchAggregatedEmojiFeedback() {
        // Este endpoint da empresa pode não precisar de token de autenticação do usuário
        // se for para dados públicos agregados. Verifique seu contrato de API.
        // Para este exemplo, não enviaremos token.

        isLoading.value = true
        apiResponseMessage.value = null // Limpa mensagens anteriores
        aggregatedEmojiFeedback.value = null // Limpa dados anteriores

        viewModelScope.launch {
            try {
                // Chama o novo método da ApiService
                val response = apiService.getAggregatedEmojiFeedback()

                if (response.isSuccessful) {
                    aggregatedEmojiFeedback.value = response.body()
                    if (aggregatedEmojiFeedback.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum feedback de emoji agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Feedback de emoji agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar feedback de emoji agregado: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (feedback de emoji agregado): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedEmojiFeedback: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchAggregatedFeelingFeedback() {
        isLoading.value = true
        apiResponseMessage.value = null
        aggregatedFeelingFeedback.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getAggregatedFeelingFeedback() // Novo método da ApiService

                if (response.isSuccessful) {
                    aggregatedFeelingFeedback.value = response.body()
                    if (aggregatedFeelingFeedback.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum feedback de sentimento agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Feedback de sentimento agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar feedback de sentimento agregado: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (feedback de sentimento agregado): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedFeelingFeedback: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchAggregatedWorkloadFeedback() {
        isLoading.value = true
        apiResponseMessage.value = null
        aggregatedWorkloadFeedback.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getAggregatedWorkloadFeedback() // Novo método da ApiService

                if (response.isSuccessful) {
                    aggregatedWorkloadFeedback.value = response.body()
                    if (aggregatedWorkloadFeedback.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum feedback de carga de trabalho agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Feedback de carga de trabalho agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar feedback de carga de trabalho: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (feedback de carga de trabalho): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedWorkloadFeedback: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchAggregatedAlertSignsFeedback() {
        isLoading.value = true
        apiResponseMessage.value = null
        aggregatedAlertSignsFeedback.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getAggregatedAlertSignsFeedback() // Novo método da ApiService

                if (response.isSuccessful) {
                    aggregatedAlertSignsFeedback.value = response.body()
                    if (aggregatedAlertSignsFeedback.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum feedback de sinais de alerta agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Feedback de sinais de alerta agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar feedback de sinais de alerta: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (feedback de sinais de alerta): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedAlertSignsFeedback: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchAggregatedClimateDiagnostics() {
        isLoading.value = true
        apiResponseMessage.value = null
        aggregatedClimateDiagnostics.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getAggregatedClimateDiagnostics() // Novo método da ApiService

                if (response.isSuccessful) {
                    aggregatedClimateDiagnostics.value = response.body()
                    if (aggregatedClimateDiagnostics.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum diagnóstico de clima agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Diagnóstico de clima agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar diagnóstico de clima: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (diagnóstico de clima): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedClimateDiagnostics: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchAggregatedCommunicationDiagnostics() {
        isLoading.value = true
        apiResponseMessage.value = null
        aggregatedCommunicationDiagnostics.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getAggregatedCommunicationDiagnostics() // Novo método da ApiService

                if (response.isSuccessful) {
                    aggregatedCommunicationDiagnostics.value = response.body()
                    if (aggregatedCommunicationDiagnostics.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum diagnóstico de comunicação agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Diagnóstico de comunicação agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar diagnóstico de comunicação: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (diagnóstico de comunicação): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedCommunicationDiagnostics: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun fetchAggregatedLeadershipDiagnostics() {
        isLoading.value = true
        apiResponseMessage.value = null
        aggregatedLeadershipDiagnostics.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getAggregatedLeadershipDiagnostics() // Novo método da ApiService

                if (response.isSuccessful) {
                    aggregatedLeadershipDiagnostics.value = response.body()
                    if (aggregatedLeadershipDiagnostics.value.isNullOrEmpty()) {
                        apiResponseMessage.value = "Nenhum diagnóstico de liderança agregado encontrado."
                    } else {
                        apiResponseMessage.value = "Diagnóstico de liderança agregado carregado."
                    }
                } else {
                    apiResponseMessage.value = "Falha ao buscar diagnóstico de liderança: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                apiResponseMessage.value = "Erro de rede (diagnóstico de liderança): ${e.message}"
                Log.e("AuthViewModel", "Erro em fetchAggregatedLeadershipDiagnostics: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }
}