package com.example.ninjacarscalculator.api

import com.example.ninjacarscalculator.models.ExchangeRates
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface ExchangeRatesApi {


    @GET("/daily_json.js")
    suspend fun exchangeRates(): Response<ExchangeRates>


    companion object{
        val retrofit by lazy {
            Retrofit
                .Builder()
                .baseUrl("https://www.cbr-xml-daily.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<ExchangeRatesApi>()
        }
    }
}
