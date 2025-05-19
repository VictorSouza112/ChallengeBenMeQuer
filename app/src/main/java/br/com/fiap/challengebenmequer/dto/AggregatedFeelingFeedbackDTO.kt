package br.com.fiap.challengebenmequer.dto

// import java.util.List; // Não é necessário para List do Kotlin

data class AggregatedFeelingFeedbackDTO(
    val feeling: String?,
    val totalCount: Int?,
    val levels: List<EmojiLevelCountDTO>? // Reutilizando o DTO anterior
)