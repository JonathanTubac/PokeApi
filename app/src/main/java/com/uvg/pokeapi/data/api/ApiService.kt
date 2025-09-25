package com.uvg.pokeapi.data.api

import com.uvg.pokeapi.data.model.Pokemons
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemons(@Path("limit") limit: Int): Response<Pokemons>

    @GET("pokemon/{name}")
    suspend fun getPokemonById(@Path("name") name: String): Response<Any>
}