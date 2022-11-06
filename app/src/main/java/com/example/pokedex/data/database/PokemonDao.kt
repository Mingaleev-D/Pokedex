package com.example.pokedex.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.model.PokemonDetailItem

/**
 * @author : Mingaleev D
 * @data : 6/11/2022
 */
@Dao
interface PokemonDao {
   //функции таблицы покемонов
   // ищет Db и возвращает результат, если имя содержит строку, предоставленную пользователем
   @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :name || '%'")
   suspend fun searchPokemonByName(name: String): List<CustomPokemonListItem>?

   // возвращает точные совпадения типов из БД
   @Query("SELECT * FROM pokemon WHERE type Like :type")
   suspend fun searchPokemonByType(type: String): List<CustomPokemonListItem>?

   @Query("SELECT * FROM pokemon")
   fun getPokemon(): List<CustomPokemonListItem>

   @Query("SELECT * FROM pokemon WHERE isSaved = 'true'")
   suspend fun getSavedPokemon(): List<CustomPokemonListItem>?

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun insertPokemonList(list: List<CustomPokemonListItem>)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertPokemon(item: CustomPokemonListItem)


   // pokemonDetails table functions
   @Query("SELECT * FROM pokemonDetails WHERE id Like :id")
   suspend fun getPokemonDetails(id: Int): PokemonDetailItem?

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertPokemonDetailsItem(pokemonDetailItem: PokemonDetailItem)

   @Query("SELECT * FROM pokemon ORDER BY id DESC LIMIT 1")
   fun getLastStoredPokemonObject(): CustomPokemonListItem
}