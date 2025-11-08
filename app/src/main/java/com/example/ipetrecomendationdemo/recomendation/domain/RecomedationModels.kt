package com.example.ipetrecomendationdemo.recomendation.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationResponse(
    @SerialName("pet_info")
    val petInfo: PetInfo,
    @SerialName("recomendacao")
    val recomendacao: Recomendacao
)

@Serializable
data class PetInfo(
    @SerialName("nome_pet")
    val nomePet: String,
    @SerialName("especie")
    val especie: String,
    @SerialName("raca")
    val raca: String,
    @SerialName("porte")
    val porte: String
)

@Serializable
data class Recomendacao(
    @SerialName("introducao")
    val introducao: String,
    @SerialName("produtos_recomendados")
    val produtosRecomendados: List<ProdutoRecomendado>
)

@Serializable
data class ProdutoRecomendado(
    @SerialName("id")
    val id: String,
    @SerialName("nome")
    val nome: String,
    @SerialName("motivo")
    val motivo: String
)
