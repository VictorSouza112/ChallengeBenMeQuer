package br.com.fiap.challengebenmequer.dto

data class AggregatedWorkloadQuestionDTO(
    val questionId: String?,
    val questionText: String?,
    val totalResponses: Int?,
    val answerStats: List<WorkloadAnswerStatsItemDTO>?
)