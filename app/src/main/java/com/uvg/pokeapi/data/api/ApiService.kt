package com.uvg.pokeapi.data.api

import com.uvg.pokeapi.data.model.Pokemons
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemons(): Response<Pokemons>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<Any>
}