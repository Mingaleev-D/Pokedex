package com.example.pokedex.di

import android.content.Context
import com.example.pokedex.data.api.ApiInterface
import com.example.pokedex.data.database.PokemonDao
import com.example.pokedex.data.database.PokemonDatabase
import com.example.pokedex.repository.MainRepository
import com.example.pokedex.repository.MainRepositoryImpl
import com.example.pokedex.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   @Singleton
   @Provides
   fun providePokeApi(): ApiInterface = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(ApiInterface::class.java)

   @Provides
   fun providePokemonDao(@ApplicationContext context: Context): PokemonDao {
      return PokemonDatabase.getDatabase(context)!!.pokemonDao()
   }

   @Singleton
   @Provides
   fun provideMainRepository(api: ApiInterface, dao: PokemonDao): MainRepository =
      MainRepositoryImpl(api, dao)
}