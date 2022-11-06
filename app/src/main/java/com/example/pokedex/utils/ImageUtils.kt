package com.example.pokedex.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.example.pokedex.R

object ImageUtils {

   fun loadImage(imageView: ImageView, url: String) {
      Glide.with(imageView.context)
         .load(url)
         .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
         .placeholder(R.drawable.place_holder_img)
         .error(R.drawable.place_holder_img)
         .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
         .into(imageView)
   }

   fun loadImageDrawable(imageView: ImageView, drawable: Int) {
      Glide.with(imageView.context)
         .load(drawable)
         .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
         .placeholder(R.drawable.place_holder_img)
         .error(R.drawable.place_holder_img)
         .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
         .into(imageView)
   }

   fun setMargins(view: View, left: Int, top: Int) {
      if (view.layoutParams is ViewGroup.MarginLayoutParams) {
         val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
         layoutParams.setMargins(left, top, 0, 0)
         view.requestLayout()
      }
   }
}