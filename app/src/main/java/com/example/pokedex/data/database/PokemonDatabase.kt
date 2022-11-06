package com.example.pokedex.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.model.Converters
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.model.PokemonDetailItem

/**
 * @author : Mingaleev D
 * @data : 6/11/2022
 */
@Database(
   entities = [CustomPokemonListItem::class, PokemonDetailItem::class],
   version = 1,
   exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {

   abstract fun pokemonDao(): PokemonDao

   companion object {
      @Volatile
      private var instance: PokemonDatabase? = null

      fun getDatabase(context: Context): PokemonDatabase? {
         return instance ?: synchronized(this) {
            val _instance = Room.databaseBuilder(
               context.applicationContext,
               PokemonDatabase::class.java,
               "Pokemon"
            )
               .fallbackToDestructiveMigration()
               .build()
            instance = _instance
            instance
         }
      }
   }
}