package com.example.pokedex.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentSavedBinding
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.ui.adapter.SavedListAdapter
import com.example.pokedex.ui.viewmodel.SavedViewModel
import com.example.pokedex.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author : Mingaleev D
 * @data : 8/11/2022
 */
@AndroidEntryPoint
class SavedListFragment : Fragment(R.layout.fragment_saved) {
   private lateinit var binding: FragmentSavedBinding
   private val viewModel: SavedViewModel by viewModels()
   private lateinit var pokemonSavedListAdapter: SavedListAdapter
   private var count = 0
   private var savedList = mutableListOf<CustomPokemonListItem>()

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentSavedBinding.bind(view)

      binding.savedFragmentBack.setOnClickListener { findNavController().popBackStack() }
      binding.listFragmentSettingsImg.setOnClickListener {
         deleteAllPokemon()
      }
      lifecycleScope.launchWhenStarted {
         setupRv()
         initObserver()
         viewModel.getSavedPokemon()
      }
   }


   private fun setupRv() {
      pokemonSavedListAdapter = SavedListAdapter()
      pokemonSavedListAdapter.setOnClickListener(object : SavedListAdapter.OnClickListener {
         override fun onClick(item: CustomPokemonListItem) {
            val bundle = Bundle()
            bundle.putParcelable("pokemon", item)
            findNavController().navigate(R.id.action_savedListFragment_to_detailFragment)
         }

      })
      pokemonSavedListAdapter.setOnDeleteListener(object : SavedListAdapter.OnDeleteListener {
         override fun onDelete(item: CustomPokemonListItem, position: Int) {
            deletePokemon(item, position)
         }

      })
      binding.savedFragmentRv.adapter = pokemonSavedListAdapter

   }

   private fun deleteAllPokemon() {
      val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
      builder.setMessage("Remove All pokemon?")
         .setCancelable(false)
         .setPositiveButton("yes") { dialog, id ->
            if (count > 0) {
               savedList.forEach {
                  it.isSaved = "false"
                  viewModel.deletePokemon(it)
               }
            }
            savedList.clear()
            pokemonSavedListAdapter.setList(savedList)
            pokemonSavedListAdapter.notifyDataSetChanged()
            binding.savedFragmentPlaceholder.isVisible = true
         }
         .setNegativeButton("No") { dialog, id ->
            dialog.dismiss()
         }
         .create()
         .show()
   }

   private fun deletePokemon(customPokemonListItem: CustomPokemonListItem, position: Int) {
      val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
      builder.setMessage("Remove pokemon?")
         .setCancelable(false)
         .setPositiveButton("yes") { dialog, id ->
            customPokemonListItem.isSaved = "false"
            pokemonSavedListAdapter.removeItemAtPosition(position)
            pokemonSavedListAdapter.notifyDataSetChanged()
            count = -1
            if (count == 0) {
               binding.savedFragmentPlaceholder.isVisible = true
            }
            viewModel.deletePokemon(customPokemonListItem)
         }
         .setNegativeButton("no") { dialog, id ->
            dialog.dismiss()
         }
         .create()
         .show()
   }

   private fun initObserver() {
      viewModel.pokemonList.observe(viewLifecycleOwner) { list ->
         when (list) {
            is Resource.Success -> {
               if (list.data?.isNotEmpty() == true) {
                  count = list.data.size
                  savedList = list.data as MutableList<CustomPokemonListItem>
                  pokemonSavedListAdapter.setList(list.data)
                  pokemonSavedListAdapter.notifyDataSetChanged()
               }
            }
            is Resource.Error -> {
               binding.savedFragmentPlaceholder.isVisible = true
            }
            is Resource.Expired -> {}
            is Resource.Loading -> {}
         }
      }
   }
}


