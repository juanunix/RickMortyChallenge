package com.example.rickmortychallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmortychallenge.data.network.CharacterApiService
import com.example.rickmortychallenge.domain.model.Character

class CharacterPagingSource(
    private val apiService: CharacterApiService,
    private val status: String? = null,
    private val name: String? = null,
    private val species: String? = null
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: 1
        return try {
            val response = apiService.getCharacters(
                page = position,
                status = status,
                name = name,
                species = species
            )
            val characters = response.results
            
            val nextKey = if (response.info.next != null) position + 1 else null
            val prevKey = if (position > 1) position - 1 else null
            
            LoadResult.Page(
                data = characters,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
