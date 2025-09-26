package com.uvg.pokeapi.data.model

data class Pokemons (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

data class Pokemon (
    val name: String,
    val url: String
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites
)

data class Sprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)