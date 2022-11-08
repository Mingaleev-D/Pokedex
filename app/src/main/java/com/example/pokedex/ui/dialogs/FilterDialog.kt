package com.example.pokedex.ui.dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.pokedex.R


/**
 * @author : Mingaleev D
 * @data : 8/11/2022
 */

class FilterDialog(var filterListener:FilterListener): DialogFragment() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setRetainInstance(true)
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {

      val fragment_container = container
      val rootView = layoutInflater.inflate(R.layout.dialog_typefilter,fragment_container,false)
      val fireImage = rootView.findViewById<ImageView>(R.id.dialog_fire_img)
      val waterImage = rootView.findViewById<ImageView>(R.id.dialog_water_img)
      val grassImage = rootView.findViewById<ImageView>(R.id.dialog_grass_img)
      val cancelBtn = rootView.findViewById<ImageView>(R.id.dialog_cancel_button)

      fireImage.setOnClickListener {
        filterListener.typeToSearch("fire")
         this.dismiss()
      }
      waterImage.setOnClickListener {
        filterListener.typeToSearch("water")
         this.dismiss()
      }
      grassImage.setOnClickListener {
        filterListener.typeToSearch("grass")
         this.dismiss()
      }
      cancelBtn.setOnClickListener {
         this.dismiss()
      }

      return rootView
   }

   interface FilterListener{
      fun typeToSearch(type: String)
   }

   override fun onDestroyView() {
      if(dialog != null && retainInstance) dialog!!.setOnDismissListener(null)
      super.onDestroyView()
   }
}