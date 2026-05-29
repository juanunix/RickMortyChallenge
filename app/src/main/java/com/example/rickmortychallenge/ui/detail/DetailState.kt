package com.example.rickmortychallenge.ui.detail

import com.example.rickmortychallenge.domain.model.Character

sealed interface DetailState {
    data object Loading : DetailState
    data class Success(val character: Character) : DetailState
    data class Error(val message: String) : DetailState
}
