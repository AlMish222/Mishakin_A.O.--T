package com.example.dz_1

import interfaces.GoogleBooksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    const val apiKey = "AIzaSyBHFXynBhdRhe1G6u0RgwVC98wuGE9vXpo"
    const val apiKey2 = "AIzaSyBHFXynBhdRhe1G6u0RgwVC98wuGE9vXpo"

    val googleBooksApi: GoogleBooksApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }
}