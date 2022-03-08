package com.fatih.popcornapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieService {
    private const val BASE_URL= "https://api.themoviedb.org/3/"
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
    val movieApi:MovieApi= getRetrofit().create(MovieApi::class.java)
}