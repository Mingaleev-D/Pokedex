package com.example.pokedex.utils

/**
 * @author : Mingaleev D
 * @data : 6/11/2022
 */

sealed class Resource<T>(
   val data:T?,
   val message:String?
) {
   class Success<T>(data: T):Resource<T>(data,null)
   class Error<T>(message: String):Resource<T>(null,message)
   class Loading<T>(message: String):Resource<T>(null,message)
   class Expired<T>(message: String,data: T):Resource<T>(data,message)
}