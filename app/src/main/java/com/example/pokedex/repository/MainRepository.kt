package com.example.pokedex.repository

import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.model.PokemonDetailItem
import com.example.pokedex.utils.Resource

/**
 * @author : Mingaleev D
 * @data : 6/11/2022
 */

interface MainRepository {
   suspend fun getPokemonList(): Resource<List<CustomPokemonListItem>>
   suspend fun getPokemonListNextPage(): Resource<List<CustomPokemonListItem>>
   suspend fun getSavePokemon(): Resource<List<CustomPokemonListItem>>
   suspend fun getPokemonDetail(id: Int): Resource<PokemonDetailItem>
   suspend fun getLastStoredPokemon(): CustomPokemonListItem
   suspend fun savePokemon(pokemonListItem: CustomPokemonListItem)
}