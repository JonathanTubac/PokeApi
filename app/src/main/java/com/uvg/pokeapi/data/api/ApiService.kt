package com.uvg.pokeapi.data.api

import com.uvg.pokeapi.data.model.PokemonDetail
import com.uvg.pokeapi.data.model.Pokemons
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemons(@Query("limit") limit: Int): Response<Pokemons>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<Any>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: String): Response<PokemonDetail>
}