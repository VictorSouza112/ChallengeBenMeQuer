package br.com.fiap.challengebenmequer.dto

data class AuthRequestDTO(
    val username: String?, // Usado para login, pode ser nulo para registro se o backend gerar
    val password: String
)