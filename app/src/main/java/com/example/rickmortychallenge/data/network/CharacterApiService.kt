package com.example.rickmortychallenge.data.network

import com.example.rickmortychallenge.data.model.CharacterResponse
import com.example.rickmortychallenge.domain.model.Character
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("status") status: String? = null
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): Character
}
