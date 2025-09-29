package com.uvg.pokeapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.pokeapi.data.model.Pokemon
import com.uvg.pokeapi.data.model.PokemonDetail
import com.uvg.pokeapi.data.repository.PokemonRepository
import com.uvg.pokeapi.getPokemonIdFromUrl
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    init {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(100)
                if (response.isSuccessful) {
                    _pokemonList.value = response.body()?.results ?: emptyList()
                } else {
                    _pokemonList.value = emptyList()
                    println("Error API: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _pokemonList.value = emptyList()
            }
        }
    }
}

