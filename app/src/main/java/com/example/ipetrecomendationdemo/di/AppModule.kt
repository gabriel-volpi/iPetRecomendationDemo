package com.example.ipetrecomendationdemo.di

import com.example.ipetrecomendationdemo.iPetRecomendationDemo
import com.example.ipetrecomendationdemo.recomendation.data.RecommendationApi
import com.example.ipetrecomendationdemo.recomendation.data.RecommendationRepository
import com.example.ipetrecomendationdemo.recomendation.data.RecommendationRepositoryImpl
import com.example.ipetrecomendationdemo.recomendation.presentation.PetProductsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {

    // já existia
    single<CoroutineScope> {
        (androidApplication() as iPetRecomendationDemo).applicationScope
    }

    // Ktor client (OkHttp) com timeouts + retry + logging
    single<HttpClient> {
        HttpClient(OkHttp) {
            // Timeouts no nível do Ktor
            install(HttpTimeout) {
                requestTimeoutMillis = 90_000    // tempo total da call
                connectTimeoutMillis = 30_000    // abrir conexão TCP/TLS
                socketTimeoutMillis = 90_000     // inatividade após conectar
            }

            // Retry com backoff exponencial (evita falhar no cold start)
            install(HttpRequestRetry) {
                retryOnExceptionOrServerErrors(maxRetries = 3)
                exponentialDelay(base = 500.0, maxDelayMs = 5_000)
            }

            // Logging (ajuda a depurar; reduza em produção)
            install(Logging) {
                level = LogLevel.INFO // use BODY se quiser ver payloads
            }

            // JSON
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }

            // Config do OkHttp (nível engine)
            engine {
                config {
                    connectTimeout(30, TimeUnit.SECONDS)
                    readTimeout(90, TimeUnit.SECONDS)
                    writeTimeout(90, TimeUnit.SECONDS)
                    retryOnConnectionFailure(true)
                }
            }
        }
    }

    // API + Repo
    single { RecommendationApi(get()) }
    single<RecommendationRepository> { RecommendationRepositoryImpl(get()) }

    // ViewModel
    viewModel { PetProductsViewModel(get()) }
}
