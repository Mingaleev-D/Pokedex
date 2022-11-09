package com.example.pokedex.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentDetailBinding
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.model.PokemonDetailItem
import com.example.pokedex.ui.viewmodel.DetailViewModel
import com.example.pokedex.utils.ImageUtils
import com.example.pokedex.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * @author : Mingaleev D
 * @data : 9/11/2022
 */
@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
   private lateinit var binding: FragmentDetailBinding
   private val viewModel: DetailViewModel by viewModels()
   private lateinit var pokemon: CustomPokemonListItem

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentDetailBinding.bind(view)
      arguments?.let {
         it.getParcelable<CustomPokemonListItem>("pokemon")?.let { mPokemon ->
            pokemon = mPokemon
            pokemon.type?.let { type -> setType(type) }
            binding.detailFragmentTitleName.text = pokemon.name.replaceFirstChar { char ->
               if (char.isLowerCase()) char.titlecase(
                  Locale.getDefault()
               ) else char.toString()
            }
            getPokemonDetails(pokemon.apiId)

            initObserver()
         }
      }
      if (this::pokemon.isInitialized) {
         if (pokemon.isSaved == "false") {
            binding.detailFragmentSaveButton.setOnClickListener {
               pokemon.isSaved = "true"
               viewModel.savePokemon(pokemon)
               Toast.makeText(requireContext(), "Pokemon saved", Toast.LENGTH_SHORT).show()
               binding.detailFragmentSaveButton.text = "Saved"
            }
         } else {
            binding.detailFragmentSaveButton.text = "Saved"
         }
      }
   }

   private fun setType(type: String) {
      binding.detailFragmentType.text = "Type : ${
         type.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
               Locale.getDefault()
            ) else it.toString()
         }
      }"
   }

   private fun getPokemonDetails(apiId: Int) {
      viewModel.getPokemonDetails(id)
   }

   private fun initObserver() {
      viewModel.pokemonDetails.observe(viewLifecycleOwner) { details ->
         when (details) {
            is Resource.Success -> {
               details.data?.let {
                  setupView(it)
               }
            }
            is Resource.Error -> {
               Toast.makeText(requireContext(), "No pokemon details found", Toast.LENGTH_SHORT)
                  .show()
            }
            is Resource.Expired -> {
               details.data?.let {
                  setupView(it)
               }
               Toast.makeText(requireContext(), "No pokemon details found", Toast.LENGTH_SHORT)
                  .show()
            }
            is Resource.Loading -> {

            }
         }
      }
   }

   private fun setupView(pokemonDetails: PokemonDetailItem) {
      pokemonDetails.sprites.otherSprites.artwork.front_default?.let {
         ImageUtils.loadImage(binding.detailFragmentImage, it)
      }
      for (i in pokemonDetails.abilities) {
         val textView = TextView(requireContext())
         textView.text =
            i.ability.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
         textView.textSize = 15F
         textView.setTextColor(Color.WHITE)
         textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
         )
         binding.abilitiesContainer.addView(textView)
      }

      val pokemonStats = mutableListOf<Int>()

      for (i in pokemonDetails.stat) {
         val textView = TextView(requireContext())
         val progressBar =
            ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal)
         progressBar.progress = i.baseStat ?: 0
         progressBar.progressTintList = ColorStateList.valueOf(Color.WHITE)

         textView.text =
            i.stat.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
         textView.textSize = 15F
         textView.setTextColor(Color.WHITE)
         textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
         )
         i.baseStat?.let { pokemonStats.add(it) }
         binding.statsContainer.addView(textView)
         binding.statsContainer.addView(progressBar)
      }

      val pokemonAerage = pokemonStats.sum() / 6
      val dp = (40 * (requireContext().resources.displayMetrics.density)).toInt()

      addStarToContainer(dp)

      if (pokemonAerage > 60){
         addStarToContainer(dp)
      }
      if(pokemonAerage>79){
         addStarToContainer(dp)
      }
      pokemon.Image?.let {
         ImageUtils.loadImage(binding.mapviewPlot,it)
      }
      ImageUtils.setMargins(
         binding.mapviewPlot,
         viewModel.plotLeft,
         viewModel.plotTop
      )
   }

   private fun addStarToContainer(dp: Int) {
      val img = ImageView(requireContext())
      val lp = LinearLayout.LayoutParams(dp,dp)
      img.layoutParams = lp
      ImageUtils.loadImageDrawable(img,R.drawable.star)
      binding.detailFragmentStarContainer.addView(img)
   }

}