package com.example.ipetrecomendationdemo.recomendation.data

import com.example.ipetrecomendationdemo.recomendation.domain.RecommendationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class RecommendationApi(
    private val client: HttpClient
) {
    suspend fun fetchRecommendations(body: RecommendationRequestBody): RecommendationResponse {
        return client.post("https://ipet-backend-js.vercel.app/api/recomendacoes/recomendacoes-ia") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }
}

@Serializable
data class RecommendationRequestBody(
    val nome_pet: String,
    val especie: String,
    val raca: String,
    val porte: String
)
