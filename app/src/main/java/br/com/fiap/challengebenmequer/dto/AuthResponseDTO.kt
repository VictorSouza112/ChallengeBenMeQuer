package br.com.fiap.challengebenmequer.dto

data class AuthResponseDTO(
    val token: String?,
    val expiresIn: Long?
)