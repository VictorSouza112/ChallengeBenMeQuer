package br.com.fiap.challengebenmequer.network

import br.com.fiap.challengebenmequer.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // MUITO IMPORTANTE:
    // Para o Emulador Android, use "http://10.0.2.2:8080/"
    // Para Dispositivo Físico na mesma rede Wi-Fi, use "http://SEU_IP_WIFI:8080/"
    private const val BASE_URL = "http://192.168.15.11:8080/"

    // Configura o interceptor de logging para OkHttp
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Loga cabeçalhos e corpo das requisições/respostas
    }

    // Configura o cliente OkHttp com o interceptor de logging e timeouts
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Adiciona o interceptor
        .connectTimeout(30, TimeUnit.SECONDS) // Timeout para conectar
        .readTimeout(30, TimeUnit.SECONDS)    // Timeout para ler dados
        .writeTimeout(30, TimeUnit.SECONDS)   // Timeout para escrever dados
        .build()

    // Cria a instância do Retrofit (lazy para ser criada apenas quando acessada pela primeira vez)
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Usa o OkHttpClient customizado
            .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para converter JSON
            .build()
        retrofit.create(ApiService::class.java) // Cria a implementação da nossa ApiService
    }
}