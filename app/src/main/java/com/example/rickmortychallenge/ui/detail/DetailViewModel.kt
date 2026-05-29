package com.example.rickmortychallenge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortychallenge.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.LoadDetail -> loadDetail(action.id)
            is DetailAction.Retry -> loadDetail(action.id)
        }
    }

    private fun loadDetail(id: Int) {
        viewModelScope.launch {
            _state.update { DetailState.Loading }
            repository.getCharacterDetails(id)
                .onSuccess { character ->
                    _state.update { DetailState.Success(character) }
                }
                .onFailure { exception ->
                    _state.update { DetailState.Error(exception.message ?: "Failed to load details") }
                }
        }
    }
}
