package com.example.ninjacarscalculator.repository

import com.example.ninjacarscalculator.api.ExchangeRatesApi.Companion.retrofit
import com.example.ninjacarscalculator.models.ExchangeRates
import retrofit2.Response

class Repository {

    suspend fun getExchangeRates(): Response<ExchangeRates> {
        return retrofit.exchangeRates()
    }
}
