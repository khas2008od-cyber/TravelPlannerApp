package com.example.travelplannerapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class CountryDto(
    val name: CountryNameDto,
    val capital: List<String>?,
    val region: String?
)

data class CountryNameDto(
    val common: String
)

interface CountriesApi {
    @GET("v3.1/all?fields=name,capital,region")
    suspend fun getCountries(): List<CountryDto>
}

object ApiClient {
    val api: CountriesApi = Retrofit.Builder()
        .baseUrl("https://pw-d20261017a29.plainwork-apps.workers.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CountriesApi::class.java)
}