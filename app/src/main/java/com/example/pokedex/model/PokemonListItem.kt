package com.example.pokedex.model

import com.google.gson.annotations.SerializedName

data class PokemonListItem(
   @SerializedName("count")
   val count: Int,

   @SerializedName("next")
   val next: String,

   @SerializedName("previous")
   val previous: String,

   @SerializedName("results")
   val results: List<PokemonResult>
)

data class PokemonResult(
   val name: String,
   val url: String
)
