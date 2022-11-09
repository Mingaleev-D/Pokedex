package com.example.pokedex.repository

import com.example.pokedex.data.api.ApiInterface
import com.example.pokedex.data.database.PokemonDao
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.model.PokemonDetailItem
import com.example.pokedex.utils.Constants
import com.example.pokedex.utils.Resource
import javax.inject.Inject

/**
 * @author : Mingaleev D
 * @data : 6/11/2022
 */

class MainRepositoryImpl @Inject constructor(
   private val pokeApi: ApiInterface,
   private val pokeDB: PokemonDao
) : MainRepository {

   private val fiveMinutesAgo = System.currentTimeMillis() - Constants.CACHE

   override suspend fun getPokemonList(): Resource<List<CustomPokemonListItem>> {
      val responseFromDB = pokeDB.getPokemon()

      if (responseFromDB.isNotEmpty()) {
         return Resource.Success(responseFromDB)
      } else {
         val preSeedList = mutableListOf<CustomPokemonListItem>()

         for (i in 1..10) {
            when (val apiResult = getPokemonDetail(1)) {
               is Resource.Success -> {
                  apiResult.data?.let { newPokemon ->
                     val newPokemonObj = CustomPokemonListItem(
                        name = newPokemon.name,
                        Image = newPokemon.sprites.front_default,
                        type = newPokemon.types?.get(0)?.type?.name.toString(),
                        positionLeft = (0..1500).random(),
                        positionTop = (0..1500).random(),
                        apiId = newPokemon.id
                     )
                     preSeedList.add(newPokemonObj)
                  }
               }
               else -> {
                  return Resource.Error("not found items")
               }
            }
         }
         pokeDB.insertPokemonList(preSeedList)
         return Resource.Success(pokeDB.getPokemon())
      }
   }

   override suspend fun getPokemonListNextPage(): Resource<List<CustomPokemonListItem>> {

      val lastStorePokemon = getLastStoredPokemon()
      val nextPokemonId = lastStorePokemon.apiId + 1
      val pokemonList = mutableListOf<CustomPokemonListItem>()

      for (i in nextPokemonId..(nextPokemonId + 9)) {
         when (val apiResult = getPokemonDetail(1)) {
            is Resource.Success -> {
               apiResult.data?.let { newPokemon ->
                  val newPokemonObj = CustomPokemonListItem(
                     name = newPokemon.name,
                     Image = newPokemon.sprites.front_default,
                     type = newPokemon.types?.get(0)?.type?.name.toString(),
                     positionLeft = (0..1500).random(),
                     positionTop = (0..1500).random(),
                     apiId = newPokemon.id
                  )
                  pokemonList.add(newPokemonObj)
               }
            }
            else -> {
               return Resource.Error("not found items")
            }
         }
      }
      pokeDB.insertPokemonList(pokemonList)
      return Resource.Success(pokemonList)

   }

   override suspend fun getSavePokemon(): Resource<List<CustomPokemonListItem>> {
      val dbResult = pokeDB.getSavedPokemon()
      return if (dbResult.isNullOrEmpty()) {
         Resource.Error("saved pokemon list is empty")
      } else {
         Resource.Success(dbResult)
      }
   }

   override suspend fun getPokemonDetail(id: Int): Resource<PokemonDetailItem> {
      val dbResult = pokeDB.getPokemonDetails(id)
      if (dbResult != null) {
         return if (dbResult.timestamp?.toLong()!! < fiveMinutesAgo) {
            getPokemonDetailFromApi(id)
         } else {
            Resource.Success(dbResult)
         }
      } else return getPokemonDetailFromApi(id)
   }

   private suspend fun getPokemonDetailFromApi(id: Int): Resource<PokemonDetailItem> {
      return try {
         val apiResult = pokeApi.getPokemonDetails(id)
         if (apiResult.isSuccessful && apiResult.body() != null) {
            val newPokemon = apiResult.body()
            newPokemon!!.timestamp = System.currentTimeMillis().toString()
            pokeDB.insertPokemonDetailsItem(newPokemon)
            Resource.Success(pokeDB.getPokemonDetails(id)!!)
         } else {
            Resource.Error(apiResult.message())
         }
      } catch (e: Exception) {
         Resource.Error("Error items")
      }
   }

   override suspend fun getLastStoredPokemon(): CustomPokemonListItem {
      return pokeDB.getLastStoredPokemonObject()
   }

   override suspend fun savePokemon(pokemonListItem: CustomPokemonListItem) {
      pokeDB.insertPokemon(pokemonListItem)
   }
}