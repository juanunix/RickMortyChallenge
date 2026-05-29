package com.example.rickmortychallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickmortychallenge.data.network.CharacterApiService
import com.example.rickmortychallenge.data.paging.CharacterPagingSource
import com.example.rickmortychallenge.domain.model.Character
import com.example.rickmortychallenge.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class NetworkCharacterRepository(
    private val apiService: CharacterApiService
) : CharacterRepository {

    override fun getCharactersStream(status: String?): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(apiService, status) }
        ).flow
    }

    override suspend fun getCharacterDetails(id: Int): Result<Character> {
        return try {
            val character = apiService.getCharacter(id)
            Result.success(character)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
