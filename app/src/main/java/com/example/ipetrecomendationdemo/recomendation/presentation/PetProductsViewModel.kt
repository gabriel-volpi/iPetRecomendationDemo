package com.example.ipetrecomendationdemo.recomendation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ipetrecomendationdemo.recomendation.data.RecommendationRepository
import com.example.ipetrecomendationdemo.recomendation.domain.RecommendationResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface PetProductsUiState {
    data object Loading : PetProductsUiState
    data class Success(val data: RecommendationResponse) : PetProductsUiState
    data class Error(val message: String) : PetProductsUiState
}

class PetProductsViewModel(
    private val repository: RecommendationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PetProductsUiState>(PetProductsUiState.Loading)
    val uiState: StateFlow<PetProductsUiState> = _uiState

    private var job: Job? = null

    fun load(nome: String, especie: String, raca: String, porte: String) {
        job?.cancel()
        _uiState.value = PetProductsUiState.Loading
        job = viewModelScope.launch {
            try {
                val resp = repository.getRecommendations(
                    nome = nome,
                    especie = especie,
                    raca = raca,
                    porte = porte
                )
                _uiState.value = PetProductsUiState.Success(resp)
            } catch (t: Throwable) {
                _uiState.value = PetProductsUiState.Error(
                    t.message ?: "Erro ao carregar recomendações"
                )
            }
        }
    }
}
