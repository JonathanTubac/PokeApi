package com.uvg.pokeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.uvg.pokeapi.data.model.Pokemon
import com.uvg.pokeapi.ui.viewmodel.PokemonViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val isDetail = currentBackStackEntry?.destination?.route?.startsWith("detail") == true
            val pokemonName = currentBackStackEntry?.arguments?.getString("pokemonName")

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (isDetail && pokemonName != null)
                                    pokemonName.replaceFirstChar { it.uppercase() }
                                else
                                    "Pokédex"
                            )
                        },
                        navigationIcon = {
                            if (isDetail) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFEF5350),
                            titleContentColor = Color.White
                        )
                    )
                } ,
                content = { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("list") {
                            PokemonListScreen(navController)
                        }
                        composable(
                            route = "detail/{pokemonId}/{pokemonName}",
                            arguments = listOf(
                                navArgument("pokemonId") { type = NavType.StringType },
                                navArgument("pokemonName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("pokemonId") ?: "1"
                            val name = backStackEntry.arguments?.getString("pokemonName") ?: "Unknown"
                            PokemonDetailScreen(id, name, navController)
                        }
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonTopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFEF5350),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val pokemons by viewModel.pokemonList.collectAsState()

    if (pokemons.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
            Text("Cargando pokemons...")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pokemons) { pokemon ->
                PokemonCard(pokemon) {
                    val id = getPokemonIdFromUrl(pokemon.url)
                    navController.navigate("detail/$id/${pokemon.name}")
                }
            }
        }
    }
}



@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    val pokemonId = getPokemonIdFromUrl(pokemon.url)
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"

    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(model = imageUrl, contentDescription = pokemon.name, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = pokemon.name.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium)
        }
    }
}


fun getPokemonIdFromUrl(url: String): String {
    return url.trimEnd('/').split("/").last()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(pokemonId: String, pokemonName: String, navController: NavController) {

    val sprites = listOf(
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/$pokemonId.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$pokemonId.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/$pokemonId.png"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Pokémon #$pokemonId",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sprites) { spriteUrl ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    AsyncImage(
                        model = spriteUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonSpriteCard(imageUrl: String, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
    }
}