package com.example.pokedex.ui.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokedex.R

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setTheme(R.style.splashTheme)
      setContentView(R.layout.activity_main)

      if(!isNetworkConnected()){
         Toast.makeText(this, getString(R.string.check_internet_connections), Toast.LENGTH_SHORT).show()
      }
   }

   private fun isNetworkConnected(): Boolean {
      val connectivityManager =
         getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val capabilities =
         connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

      if (capabilities != null) {
         if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
               (NetworkCapabilities.TRANSPORT_CELLULAR)
            )
         ) {
            return true
         }
      }
      return false
   }
}