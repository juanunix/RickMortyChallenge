package com.example.rickmortychallenge.ui.viewmodel

sealed interface CharacterEvent {
    data class NavigateToDetail(val characterId: Int) : CharacterEvent
    data class ShowToast(val message: String) : CharacterEvent
}
