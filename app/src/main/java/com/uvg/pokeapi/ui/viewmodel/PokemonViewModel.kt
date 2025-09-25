package com.uvg.pokeapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.uvg.pokeapi.data.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers

class PokemonViewModel : ViewModel() {
    private val repository = PokemonRepository()

    val pokemonList = liveData(Dispatchers.IO) {
        val response = repository.getPokemonList()
        emit(response.body()?.results ?: emptyList())
    }
}
