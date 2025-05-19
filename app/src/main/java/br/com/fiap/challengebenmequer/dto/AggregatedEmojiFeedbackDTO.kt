package br.com.fiap.challengebenmequer.dto

data class AggregatedEmojiFeedbackDTO(
    val emoji: String?,
    val totalCount: Int?,
    val levels: List<EmojiLevelCountDTO>?
)