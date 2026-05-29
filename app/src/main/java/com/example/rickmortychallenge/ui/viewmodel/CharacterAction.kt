package com.example.rickmortychallenge.ui.viewmodel

sealed interface CharacterAction {
    data object LoadCharacters : CharacterAction
    data object Retry : CharacterAction
    data object Refresh : CharacterAction
    data class FilterByStatus(val status: String?) : CharacterAction
    data class SelectCharacter(val id: Int) : CharacterAction
}
