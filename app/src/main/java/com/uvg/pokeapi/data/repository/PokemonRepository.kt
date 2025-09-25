package com.uvg.pokeapi.data.repository

import com.uvg.pokeapi.data.api.ApiService
import com.uvg.pokeapi.data.api.RetrofitClient

class PokemonRepository {
    private val api = RetrofitClient.instance.create(ApiService::class.java)

    suspend fun getPokemonList(limit: Int) = api.getPokemons(limit)
    suspend fun getPokemonById(name: String) = api.getPokemonById(name)
}
