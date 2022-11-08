package com.example.pokedex.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ListRowItemBinding
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.utils.ImageUtils.loadImage
import java.util.*

/**
 * @author : Mingaleev D
 * @data : 8/11/2022
 */

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {

   private var onClickListener: OnClickListener? = null
   private var pokemonList = mutableListOf<CustomPokemonListItem>()

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val binding = ListRowItemBinding.inflate(inflater, parent, false)
      return MyViewHolder(binding)
   }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bind(pokemonList[position], onClickListener)
   }

   override fun getItemCount(): Int = pokemonList.size

   class MyViewHolder(private val binding: ListRowItemBinding) :
      RecyclerView.ViewHolder(binding.root) {

      @SuppressLint("SetTextI18n")
      fun bind(item: CustomPokemonListItem, onClickListener: OnClickListener?) {
         binding.rowCardTitle.text = item.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
               Locale.ROOT
            ) else it.toString()
         }
         binding.rowCardType.text = "Type: ${
            item.type.replaceFirstChar {
               if (it.isLowerCase()) it.titlecase(
                  Locale.ROOT
               ) else it.toString()
            }
         }"
         binding.cardView.setOnClickListener {
            onClickListener?.onClick(item)
         }
         item.Image?.let { loadImage(binding.rowCardImage, it) }
      }

   }

   interface OnClickListener {
      fun onClick(item: CustomPokemonListItem)
   }

   fun setOnClickListener(onClickListener: OnClickListener) {
      this.onClickListener = onClickListener
   }

   fun updateList(list: List<CustomPokemonListItem>) {
      pokemonList.addAll(list)
   }

   @SuppressLint("NotifyDataSetChanged")
   fun submitList(list: List<CustomPokemonListItem>) {
      pokemonList = list as MutableList<CustomPokemonListItem>
      notifyDataSetChanged()
   }
}