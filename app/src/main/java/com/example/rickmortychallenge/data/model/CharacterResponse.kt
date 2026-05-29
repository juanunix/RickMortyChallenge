package com.example.rickmortychallenge.data.model

import com.example.rickmortychallenge.domain.model.Character

data class CharacterResponse(
    val info: CharacterInfo,
    val results: List<Character>
)
