package com.example.rickmortychallenge.domain.repository

import androidx.paging.PagingData
import com.example.rickmortychallenge.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersStream(status: String? = null): Flow<PagingData<Character>>
    suspend fun getCharacterDetails(id: Int): Result<Character>
}
