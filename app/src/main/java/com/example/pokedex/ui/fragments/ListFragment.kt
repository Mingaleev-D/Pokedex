package com.example.pokedex.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentListBinding
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.ui.adapter.PokemonListAdapter
import com.example.pokedex.ui.dialogs.FilterDialog
import com.example.pokedex.ui.viewmodel.ListViewModel
import com.example.pokedex.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author : Mingaleev D
 * @data : 9/11/2022
 */
@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), FilterDialog.FilterListener {

   private lateinit var binding: FragmentListBinding
   private val viewModel: ListViewModel by viewModels()
   private lateinit var pokemonListAdapter: PokemonListAdapter
   private var pokemonList = mutableListOf<CustomPokemonListItem>()
   private var shouldPaginate = true

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentListBinding.bind(view)

      setupRv()
      setupClicks()
      setupSearchView()
      setupFabBtn()
      initObserver()
   }

   private fun setupFabBtn() {
      binding.listFragmentMapFAB.setOnClickListener {
         findNavController().navigate(R.id.action_listFragment_to_mapViewFragment)
      }
      binding.listFragmentSavedFAB.setOnClickListener {
         findNavController().navigate(R.id.action_listFragment_to_savedListFragment)
      }
   }

   private fun setupRv() {
      pokemonListAdapter = PokemonListAdapter()
      pokemonListAdapter.setOnClickListener(object : PokemonListAdapter.OnClickListener {
         override fun onClick(item: CustomPokemonListItem) {
            val bundle = Bundle()
            bundle.putParcelable("pokemon", item)
            findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
         }
      })
      binding.listFragmentRv.apply {
         adapter = pokemonListAdapter
         addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
               super.onScrollStateChanged(recyclerView, newState)
               if (!recyclerView.canScrollVertically(1) && binding.listFragmentSearchView.query.isEmpty()) {
                  binding.listFragmentPaginateProgress.visibility = View.VISIBLE
                  viewModel.getNextPage()
               }
            }
         })
      }
      binding.listFragmentSwipeToRefresh.setOnRefreshListener {
         if (binding.listFragmentSearchView.query.isEmpty()) {
            viewModel.getPokemonList()
         } else {
            binding.listFragmentSwipeToRefresh.isRefreshing = false
         }
      }
   }

   override fun onResume() {
      super.onResume()
      viewModel.getPokemonList()
   }

   private fun setupClicks() {
      binding.listFragmentFilterImg.setOnClickListener {
         val dialog = FilterDialog(this)
         val transaction = childFragmentManager.beginTransaction()
         transaction.add(dialog, "filter-dialog")
         transaction.commit()
      }
   }

   private fun setupSearchView() {
      binding.listFragmentSearchView.setOnClickListener {
         if (binding.listFragmentSearchView.isEmpty()) {
            pokemonListAdapter.submitList(mutableListOf())
            viewModel.getPokemonList()
         }
      }
      binding.listFragmentSearchView.setOnQueryTextListener(object :
         SearchView.OnQueryTextListener {
         override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
               pokemonListAdapter.submitList(filterListByName(query))
            } else {
               pokemonListAdapter.submitList(mutableListOf())
               viewModel.getPokemonList()
            }
            return false
         }

         override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
               pokemonListAdapter.submitList(filterListByName(newText))
            }
            return false
         }

      })
   }

   private fun initObserver() {
      viewModel.pokemonList.observe(viewLifecycleOwner) { list ->
         when (list) {
            is Resource.Success -> {
               if (list.data?.isNotEmpty() == true) {
                  pokemonList = list.data as ArrayList<CustomPokemonListItem>
                  pokemonListAdapter.updateList(list.data)
                  pokemonListAdapter.notifyDataSetChanged()
                  showProgressBar(false)

                  if(binding.listFragmentSwipeToRefresh.isRefreshing){
                     binding.listFragmentSwipeToRefresh.isRefreshing = false
                  }
               }else{
                  showProgressBar(false)
                  showEmpryRecyclerView()
               }
            }
            is Resource.Error -> {
               showProgressBar(false)
               showEmpryRecyclerView()
            }
            is Resource.Expired -> {}
            is Resource.Loading -> {
               showProgressBar(true)

            }
         }
      }
   }

   private fun showProgressBar(b: Boolean) {
      binding.listFragmentProgress.isVisible = b
      binding.listFragmentPaginateProgress.visibility = View.GONE
   }

   private fun showEmpryRecyclerView() {
      Toast.makeText(requireContext(), "No items founds", Toast.LENGTH_SHORT).show()
   }

   override fun typeToSearch(type: String) {
      shouldPaginate = false
      pokemonListAdapter.submitList(filterListByType(type))
   }

   private fun filterListByType(type: String): List<CustomPokemonListItem> {
      return pokemonList.filter { it.type == type }
   }

   private fun filterListByName(name: String): List<CustomPokemonListItem> {
      return pokemonList.filter { it.type.contains(name) }
   }
}