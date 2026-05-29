package com.example.rickmortychallenge.ui.viewmodel

import androidx.paging.PagingData
import com.example.rickmortychallenge.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CharacterState(
    val pagingDataFlow: Flow<PagingData<Character>> = emptyFlow(),
    val filterStatus: String? = null
)
