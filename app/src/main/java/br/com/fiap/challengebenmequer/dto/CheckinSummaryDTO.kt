package br.com.fiap.challengebenmequer.dto

// DTO para representar o resumo de check-ins
// Deve espelhar a estrutura que a API /v1/checkins/summary retorna.
data class CheckinSummaryDTO(
    val startDate: String?,
    val endDate: String?,
    val predominantEmoji: String?,
    val predominantFeeling: String?
)