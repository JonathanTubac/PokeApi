package com.uvg.pokeapi.data.model

data class Pokemons (
    val count: Int,
    val results: List<Pokemon>
)

data class Pokemon (
    val name: String,
    val url: String
)