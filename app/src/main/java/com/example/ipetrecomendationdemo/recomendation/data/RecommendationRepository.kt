package com.example.ipetrecomendationdemo.recomendation.data

import com.example.ipetrecomendationdemo.recomendation.domain.RecommendationResponse

interface RecommendationRepository {
    suspend fun getRecommendations(
        nome: String,
        especie: String,
        raca: String,
        porte: String
    ): RecommendationResponse
}

class RecommendationRepositoryImpl(
    private val api: RecommendationApi
) : RecommendationRepository {
    override suspend fun getRecommendations(
        nome: String,
        especie: String,
        raca: String,
        porte: String
    ): RecommendationResponse {
        val body = RecommendationRequestBody(
            nome_pet = nome,
            especie = especie,
            raca = raca,
            porte = porte
        )
        return api.fetchRecommendations(body)
    }
}
