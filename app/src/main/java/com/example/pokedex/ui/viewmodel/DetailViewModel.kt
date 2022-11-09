package com.example.pokedex.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.model.PokemonDetailItem
import com.example.pokedex.repository.MainRepository
import com.example.pokedex.utils.Resource
import com.example.pokedex.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author : Mingaleev D
 * @data : 8/11/2022
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
   private val repository: MainRepository
) : ViewModel() {

   private val _pokemonDetails = SingleLiveEvent<Resource<PokemonDetailItem>>()
   val pokemonDetails: LiveData<Resource<PokemonDetailItem>> get() = _pokemonDetails

   val plotLeft = (0..600).random()
   val plotTop = (0..600).random()

   fun getPokemonDetails(id: Int) {
      _pokemonDetails.postValue(Resource.Loading(""))
      viewModelScope.launch(Dispatchers.IO) {
         _pokemonDetails.postValue(repository.getPokemonDetail(id))
      }
   }

   fun savePokemon(customPokemonListItem: CustomPokemonListItem) =
      viewModelScope.launch(Dispatchers.IO) {
         repository.savePokemon(customPokemonListItem)
      }
}