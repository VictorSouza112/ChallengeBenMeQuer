package br.com.fiap.challengebenmequer.network

import br.com.fiap.challengebenmequer.dto.AggregatedEmojiFeedbackDTO
import br.com.fiap.challengebenmequer.dto.AggregatedFeelingFeedbackDTO
import br.com.fiap.challengebenmequer.dto.AggregatedScaleQuestionDTO
import br.com.fiap.challengebenmequer.dto.AggregatedWorkloadQuestionDTO
import br.com.fiap.challengebenmequer.dto.AuthRequestDTO
import br.com.fiap.challengebenmequer.dto.AuthResponseDTO
import br.com.fiap.challengebenmequer.dto.CheckinHistoryItemDTO
import br.com.fiap.challengebenmequer.dto.CheckinSummaryDTO
import br.com.fiap.challengebenmequer.dto.UsuarioMeDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("v1/auth/register")
    suspend fun register(@Body request: AuthRequestDTO): Response<AuthResponseDTO>

    @POST("v1/auth/login")
    suspend fun login(@Body request: AuthRequestDTO): Response<AuthResponseDTO>

    @GET("v1/users/me")
    suspend fun getMyProfile(@Header("Authorization") token: String): Response<UsuarioMeDTO>

    @GET("v1/checkins/history")
    suspend fun getCheckinHistory(@Header("Authorization") token: String): Response<List<CheckinHistoryItemDTO>>

    @GET("v1/checkins/summary")
    suspend fun getCheckinSummary(
        @Header("Authorization") token: String,
        @Query("period") period: String
    ): Response<CheckinSummaryDTO>

    @GET("v1/company/feedback/emoji")
    suspend fun getAggregatedEmojiFeedback(): Response<List<AggregatedEmojiFeedbackDTO>>

    @GET("v1/company/feedback/feeling")
    suspend fun getAggregatedFeelingFeedback(): Response<List<AggregatedFeelingFeedbackDTO>>

    @GET("v1/company/feedback/workload")
    suspend fun getAggregatedWorkloadFeedback(): Response<List<AggregatedWorkloadQuestionDTO>>

    @GET("v1/company/feedback/alert-signs")
    suspend fun getAggregatedAlertSignsFeedback(): Response<List<AggregatedWorkloadQuestionDTO>> // Reutilizando o DTO

    @GET("v1/company/diagnostics/climate")
    suspend fun getAggregatedClimateDiagnostics(): Response<List<AggregatedScaleQuestionDTO>>

    @GET("v1/company/diagnostics/communication")
    suspend fun getAggregatedCommunicationDiagnostics(): Response<List<AggregatedScaleQuestionDTO>>

    @GET("v1/company/diagnostics/leadership")
    suspend fun getAggregatedLeadershipDiagnostics(): Response<List<AggregatedScaleQuestionDTO>>
}