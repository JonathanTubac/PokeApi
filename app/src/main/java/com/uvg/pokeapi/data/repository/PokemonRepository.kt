package com.uvg.pokeapi.data.repository

import com.uvg.pokeapi.data.api.ApiService
import com.uvg.pokeapi.data.api.RetrofitClient

class PokemonRepository {
    private val api = RetrofitClient.instance.create(ApiService::class.java)

    suspend fun getPokemonList() = api.getPokemons()
    suspend fun getPokemonById(id: Int) = api.getPokemonById(id)
}
