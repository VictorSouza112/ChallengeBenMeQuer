package br.com.fiap.challengebenmequer.dto

data class AggregatedScaleQuestionDTO(
    val questionId: String?,
    val questionText: String?,
    val averageScore: Double?,
    val totalResponses: Int?,
    val classification: String? // "Atenção", "Zona de Alerta", "Ambiente Saudável"
)