package com.example.rickmortychallenge.ui.screens

import kotlinx.serialization.Serializable

@Serializable
object CharacterListRoute

@Serializable
data class CharacterDetailRoute(val characterId: Int)
