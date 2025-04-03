package com.example.data.naver.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NaverApiClient {
    private const val BASE_URL = "https://openapi.naver.com/"

    val apiService : NaverApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverApiService::class.java)
    }

}