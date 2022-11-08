package com.example.pokedex.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ListSavedRowItemBinding
import com.example.pokedex.model.CustomPokemonListItem
import com.example.pokedex.utils.ImageUtils
import java.util.*

/**
 * @author : Mingaleev D
 * @data : 8/11/2022
 */

class SavedListAdapter : RecyclerView.Adapter<SavedListAdapter.MyViewHolder>() {

   private var onClickListener: OnClickListener? = null
   private var onDeleteListener: OnDeleteListener? = null
   private var pokemonList = mutableListOf<CustomPokemonListItem>()

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val binding = ListSavedRowItemBinding.inflate(inflater, parent, false)
      return MyViewHolder(binding)
   }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bind(pokemonList[position], onClickListener, onDeleteListener, position)
   }

   override fun getItemCount(): Int = pokemonList.size

   class MyViewHolder(private val binding: ListSavedRowItemBinding) :
      RecyclerView.ViewHolder(binding.root) {

      @SuppressLint("SetTextI18n")
      fun bind(
         item: CustomPokemonListItem,
         onClickListener: OnClickListener?,
         onDeleteListener: OnDeleteListener?,
         position: Int
      ) {
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
         item.Image?.let { ImageUtils.loadImage(binding.rowCardImage, it) }

         binding.rowDeleteImg.setOnClickListener {
            onDeleteListener?.onDelete(item, position)
         }
      }

   }

   interface OnClickListener {
      fun onClick(item: CustomPokemonListItem)
   }

   interface OnDeleteListener {
      fun onDelete(item: CustomPokemonListItem, position: Int)
   }

   fun setOnClickListener(onClickListener: OnClickListener) {
      this.onClickListener = onClickListener
   }

   fun setOnDeleteListener(onDeleteListener: OnDeleteListener) {
      this.onDeleteListener = onDeleteListener
   }

   fun removeItemAtPosition(position: Int) {
      pokemonList.removeAt(position)
   }

   fun setList(list: List<CustomPokemonListItem>) {
      pokemonList.clear()
      pokemonList - (list as MutableList<CustomPokemonListItem>).toSet()
   }
}