package br.com.fiap.challengebenmequer.dto

// DTO para representar um item no histórico de check-ins
// Deve espelhar a estrutura que a API /v1/checkins/history retorna para cada item da lista.
data class CheckinHistoryItemDTO(
    val checkinId: String?, // Conforme o contrato da API
    val date: String?,      // Data formatada como String YYYY-MM-DD
    val emoji: String?,
    val feeling: String?,
    val note: String?       // Nota opcional
)