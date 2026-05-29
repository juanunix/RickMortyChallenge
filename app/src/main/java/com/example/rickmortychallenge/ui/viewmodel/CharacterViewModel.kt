package com.example.rickmortychallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickmortychallenge.domain.repository.CharacterRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterState())
    val state: StateFlow<CharacterState> = _state.asStateFlow()

    private val _events = Channel<CharacterEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        onAction(CharacterAction.LoadCharacters)
    }

    fun onAction(action: CharacterAction) {
        when (action) {
            is CharacterAction.LoadCharacters -> loadCharacters()
            is CharacterAction.Retry -> loadCharacters()
            is CharacterAction.Refresh -> loadCharacters()
            is CharacterAction.FilterByStatus -> filterByStatus(action.status)
            is CharacterAction.SelectCharacter -> selectCharacter(action.id)
        }
    }

    private fun loadCharacters() {
        val currentFilter = _state.value.filterStatus
        val pagingFlow = repository.getCharactersStream(currentFilter)
            .cachedIn(viewModelScope)
        
        _state.update {
            it.copy(pagingDataFlow = pagingFlow)
        }
    }

    private fun filterByStatus(status: String?) {
        if (_state.value.filterStatus == status) return
        
        _state.update {
            it.copy(filterStatus = status)
        }
        loadCharacters()
    }

    private fun selectCharacter(id: Int) {
        viewModelScope.launch {
            _events.send(CharacterEvent.NavigateToDetail(id))
        }
    }
}
