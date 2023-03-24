package com.example.conversormonetario

import com.example.conversormonetario.model.CurrencyResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//ENDPOINT
interface ApiLayerService {
    @GET("convert")
    fun searchConvert(
        @Query("apikey") apikey: String,
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: Int
    ) : Call<CurrencyResultData>
}