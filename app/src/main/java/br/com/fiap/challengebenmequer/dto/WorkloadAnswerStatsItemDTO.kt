package br.com.fiap.challengebenmequer.dto

data class WorkloadAnswerStatsItemDTO(
    val answerValue: String?,
    val totalCountForAnswer: Int?,
    val levelDistribution: List<AnswerLevelDetailDTO>?
)