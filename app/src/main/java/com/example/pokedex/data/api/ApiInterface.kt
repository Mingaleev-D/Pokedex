package com.example.pokedex.data.api

import com.example.pokedex.model.PokemonDetailItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author : Mingaleev D
 * @data : 6/11/2022
 */

interface ApiInterface {

   @GET("api/v2/pokemon/{id}")
   suspend fun getPokemonDetails(
      @Path("id") id:Int
   ):Response<PokemonDetailItem>
}